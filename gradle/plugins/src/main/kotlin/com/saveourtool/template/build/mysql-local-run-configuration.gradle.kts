package com.saveourtool.template.build

import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.create

interface MysqlLocalRunExtension {
    val rootPassword: Property<String>
    val user: Property<String>
    val password: Property<String>
    val databaseName: Property<String>
    val liquibaseChangelogPath: RegularFileProperty
    val liquibaseChangelogDir: DirectoryProperty
    val contexts: ListProperty<String>
}

val extension: MysqlLocalRunExtension = extensions.create("mysqlLocalRun")

val rootPassword: String by lazy {
    extension.password.getOrElse("123")
}
val user: String by lazy {
    extension.user.getOrElse("root")
}
val password: String by lazy {
    extension.password.getOrElse(rootPassword)
}
val databaseName: String by lazy {
    extension.databaseName.getOrElse("test")
}

afterEvaluate {
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
            |      - "MYSQL_USER=$user"
            |      - "MYSQL_PASSWORD=$password"
            |      - "MYSQL_DATABASE=$databaseName"
            """.trimMargin()
    ).also { startTask ->
        registerLiquibaseUpdateTask().also {
            startTask.configure { finalizedBy(it) }
        }
    }
}

fun Project.registerLiquibaseUpdateTask(
): TaskProvider<Exec> = tasks.register<Exec>("liquibaseUpdate") {
    val liquibaseChangelogPath = extension.liquibaseChangelogPath.get().asFile
    val liquibaseChangelogDir =
        extension.liquibaseChangelogDir.map { it.asFile }.getOrElse(liquibaseChangelogPath.parentFile)

    val contextsArgs = extension.contexts.orNull
        ?.takeUnless { it.isEmpty() }
        ?.joinToString(",", prefix = "--contexts")
        ?.let { listOf(it) }
        ?: emptyList()
    commandLine(
        listOf(
            "docker", "run",
            "-v", "$liquibaseChangelogDir:/liquibase/changelog",
            "--rm",
            "--env", "INSTALL_MYSQL=true",
            "--network", "mysql_default",
            "liquibase/liquibase:4.20",
            "--url=jdbc:mysql://mysql:3306/$databaseName",
            "--changeLogFile=${liquibaseChangelogPath.relativeTo(liquibaseChangelogDir)}",
            "--username=$user",
            "--password=$password",
            "--log-level=info",
        ) + contextsArgs + listOf("update")
    )
}