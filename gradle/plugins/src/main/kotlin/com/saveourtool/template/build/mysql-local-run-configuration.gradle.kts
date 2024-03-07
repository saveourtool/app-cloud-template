package com.saveourtool.template.build

import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.create
import org.intellij.lang.annotations.Language

interface MysqlLocalRunExtension {
    val rootPassword: Property<String>
    val user: Property<String>
    val password: Property<String>
    val startupPath: Property<String>
}

val extension: MysqlLocalRunExtension = extensions.create("mysqlLocalRun")

afterEvaluate {
    val rootPassword: String = extension.password.getOrElse("123")

    registerDockerService(
        serviceName = "mysql",
        startupDelayInMillis = DEFAULT_STARTUP_TIMEOUT,
        dockerComposeContent = """
            |  mysql:
            |    image: mysql:8.0.28-oracle
            |    container_name: mysql
            |    ports:
            |      - "3306:3306"
            |    environment:
            |      - "MYSQL_ROOT_PASSWORD=$rootPassword"
            |    command: ["--log_bin_trust_function_creators=1"]
            """.trimMargin()
    ).also { startTask ->
        extension.startupPath.orNull?.let { startupPath ->
            registerMinioStartupTask(
                "bucketName",
                "user",
                "password",
                startupPath,
            ).also {
                startTask.configure { finalizedBy(it) }
            }
        }
    }
}

fun Project.registerMinioStartupTask(
    bucketName: String,
    user: String,
    password: String,
    startupPath: String,
): TaskProvider<Exec> = tasks.register<Exec>("minioStartup") {
    val workingDirectory = layout.buildDirectory.dir("minio-startup")
    val runScriptProvider = workingDirectory.map { it.file("run.sh") }
    outputs.file(runScriptProvider)

    @Language("sh")
    val shellScript: String = """
        #!/bin/sh
        /usr/bin/mc alias set minio http://host.docker.internal:9000 $user $password
        /usr/bin/mc mb --ignore-existing minio/$bucketName
        /usr/bin/mc policy set public minio/$bucketName
        /usr/bin/mc cp --recursive /data/ minio/$bucketName
    """.trimIndent()

    doFirst {
        runScriptProvider.get().asFile.writeText(shellScript)
    }

    commandLine(
        "docker", "run",
        "-v", "${project.rootProject.layout.projectDirectory.asFile}/$startupPath:/data",
        "-v", "${workingDirectory.get().asFile}:/run",
        "--rm",
        "--entrypoint=/run/run.sh",
        "minio/mc:latest",
    )
}