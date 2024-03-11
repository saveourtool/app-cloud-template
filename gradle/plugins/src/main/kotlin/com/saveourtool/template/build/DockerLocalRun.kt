package com.saveourtool.template.build

import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.TaskProvider
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.register
import org.intellij.lang.annotations.Language
import java.io.ByteArrayOutputStream

const val DEFAULT_STARTUP_TIMEOUT = 5_000L

internal fun Project.registerDockerService(
    serviceName: String,
    startupDelayInMillis: Long,
    @Language("yaml")
    dockerComposeContent: String,
): TaskProvider<Exec> = registerDockerService(
    serviceName = serviceName,
    startupDelayInMillis = startupDelayInMillis,
    dockerComposeContentProvider = providers.provider {
        dockerComposeContent
    }
)

/**
 * Registers tasks to start [serviceName] specified in [dockerComposeContentProvider]
 *
 * @param serviceName
 * @param startupDelayInMillis
 * @param dockerComposeContentProvider
 * @return task which starts [serviceName]
 */
internal fun Project.registerDockerService(
    serviceName: String,
    startupDelayInMillis: Long,
    @Language("yaml")
    dockerComposeContentProvider: Provider<String>,
): TaskProvider<Exec> {
    val serviceNameCapitalized = serviceName.split("-").joinToString("") { it.capitalized() }
    val composeFileProvider: Provider<RegularFile> = layout.buildDirectory.dir(serviceName).map { it.file("docker-compose.yaml") }
    val generateComposeFileTaskName = "generateComposeFileFor$serviceNameCapitalized"
    tasks.register(generateComposeFileTaskName) {
        description = "Generate a compose file for $serviceName service"
        outputs.file(composeFileProvider)
        doLast {
            composeFileProvider.get().asFile
                .writeText("""
                    |version: '3.9'
                    |services:
                    |${dockerComposeContentProvider.get().prependIndent(  )}
                    """.trimMargin())
        }
    }

    val startServiceTaskName = "start${serviceNameCapitalized}Service"
    return tasks.register<Exec>(startServiceTaskName) {
        description = "Start $serviceName locally as a service"
        dependsOn(generateComposeFileTaskName)
        doFirst {
            logger.lifecycle("Running the following command: [docker compose --file ${composeFileProvider.get()} up -d $serviceName]")
        }
        standardOutput = ByteArrayOutputStream()
        errorOutput = ByteArrayOutputStream()
        commandLine("docker", "compose", "--file", composeFileProvider.get(), "up", "-d", serviceName)
        isIgnoreExitValue = true
        doLast {
            val execResult = executionResult.get()
            if (execResult.exitValue != 0) {
                logger.lifecycle("${this.name} failed with following output:")
                logger.info(standardOutput.toString())
                logger.error(errorOutput.toString())
                execResult.assertNormalExitValue()
                execResult.rethrowFailure()
            }
            logger.lifecycle("Waiting $startupDelayInMillis millis for $serviceName to start")
            Thread.sleep(startupDelayInMillis)
        }
    }
}
