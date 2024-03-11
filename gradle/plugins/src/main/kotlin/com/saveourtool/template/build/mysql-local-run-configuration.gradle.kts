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

val rootPasswordProvider: Provider<String> = extension.password.orElse("123")
val databaseNameProvider: Provider<String> = extension.databaseName.orElse("test")
val userProvider: Provider<String> = extension.user.orElse("root")
val passwordProvider: Provider<String> = extension.password.orElse(rootPasswordProvider)

registerDockerService(
    serviceName = "mysql",
    startupDelayInMillis = DEFAULT_STARTUP_TIMEOUT,
    dockerComposeContentProvider = provider {
        val isNotRootUser = userProvider.map { !it.equals("root", ignoreCase = true) }.get()
        """
        |  mysql:
        |    image: mysql:8.0.28-oracle
        |    container_name: mysql
        |    ports:
        |      - "3306:3306"
        |    environment:
        |      - "MYSQL_ROOT_PASSWORD=${rootPasswordProvider.get()}"
        |      - "MYSQL_DATABASE=${databaseNameProvider.get()}"
        |      ${if (isNotRootUser) "- \"MYSQL_USER=${userProvider.get()}\"" else ""}
        |      ${if (isNotRootUser) "- \"MYSQL_PASSWORD=${passwordProvider.get()}\"" else ""}
        """.trimMargin()
    }
).also { startTask ->
    registerLiquibaseUpdateTask().also {
        startTask.configure { finalizedBy(it) }
    }
}

fun Project.registerLiquibaseUpdateTask(): TaskProvider<Exec> = tasks.register<Exec>("liquibaseUpdate") {
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
            // <mysql>_default -- takes from service name
            "--network", "mysql_default",
            "liquibase/liquibase:4.20",
            "--url=jdbc:mysql://mysql:3306/${databaseNameProvider.get()}",
            "--changeLogFile=${liquibaseChangelogPath.relativeTo(liquibaseChangelogDir)}",
            "--username=${userProvider.get()}",
            "--password=${passwordProvider.get()}",
            "--log-level=info",
        ) + contextsArgs + listOf("update")
    )
}