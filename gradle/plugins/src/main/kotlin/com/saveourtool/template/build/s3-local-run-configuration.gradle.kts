package com.saveourtool.template.build

import org.intellij.lang.annotations.Language

interface S3LocalRunExtension {
    val bucketName: Property<String>
    val user: Property<String>
    val password: Property<String>
    val startupPath: Property<String>
}

val extension: S3LocalRunExtension = extensions.create("s3LocalRun")

afterEvaluate {
    val bucketName: String = extension.bucketName.getOrElse("test")
    val user: String = extension.user.getOrElse("admin")
    val password: String = extension.password.getOrElse("adminadmin")

    registerDockerService(
        serviceName = "minio",
        startupDelayInMillis = DEFAULT_STARTUP_TIMEOUT,
        dockerComposeContent = """
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
    ).also { startTask ->
        extension.startupPath.orNull?.let { startupPath ->
            registerMinioStartupTask(
                bucketName,
                user,
                password,
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