package com.saveourtool.template.build

import org.intellij.lang.annotations.Language

interface S3LocalRunExtension {
    val bucketName: Property<String>
    val user: Property<String>
    val password: Property<String>
    val startupPath: DirectoryProperty
}

val extension: S3LocalRunExtension = extensions.create("s3LocalRun")

val userProvider: Provider<String> = extension.user.orElse("admin")
val passwordProvider: Provider<String> = extension.user.orElse("adminadmin")
afterEvaluate {
    registerDockerService(
        serviceName = "minio",
        startupDelayInMillis = DEFAULT_STARTUP_TIMEOUT,
        dockerComposeContentProvider = userProvider.zip(passwordProvider) { user, password ->
            """
            |minio:
            |  image: minio/minio:latest
            |  container_name: minio
            |  command: server /data --console-address ":9090"
            |  ports:
            |    - 9000:9000
            |    - 9090:9090
            |  environment:
            |    MINIO_ROOT_USER: $user
            |    MINIO_ROOT_PASSWORD: $password
            """.trimMargin()
        }
    ).also { startTask ->
        registerMinioStartupTask().also {
            startTask.configure { finalizedBy(it) }
        }
    }
}

fun Project.registerMinioStartupTask(): TaskProvider<Exec> = tasks.register<Exec>("minioStartup") {
    val startupPath = extension.startupPath.map { it.asFile.absolutePath }. orNull
    enabled = startupPath?.let { true } ?: false
    val workingDirectory = layout.buildDirectory.dir("minio-startup")
    val runScriptProvider = workingDirectory.map { it.file("run.sh") }
    outputs.file(runScriptProvider)

    val bucketName: String = extension.bucketName.getOrElse("test")
    @Language("sh")
    val shellScript: String = """
        #!/bin/sh
        /usr/bin/mc alias set minio http://minio:9000 ${userProvider.get()} ${passwordProvider.get()}
        /usr/bin/mc mb --ignore-existing minio/$bucketName
        /usr/bin/mc policy set public minio/$bucketName
        /usr/bin/mc cp --recursive /data/ minio/$bucketName
    """.trimIndent()

    doFirst {
        runScriptProvider.get().asFile.writeText(shellScript)
    }

    commandLine(
        "docker", "run",
        "-v", "$startupPath:/data",
        "-v", "${workingDirectory.get().asFile}:/run",
        "--network", "minio_default",
        "--rm",
        "--entrypoint=/run/run.sh",
        "minio/mc:latest",
    )
}