plugins {
    id("com.saveourtool.template.build.spring-boot-kotlin-configuration")
    id("com.saveourtool.template.build.mysql-local-run-configuration")
}

dependencies {
    implementation(projects.common)
    implementation(projects.authenticationUtils)
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}

mysqlLocalRun {
    databaseName = "gateway"
    liquibaseChangelogPath = project.layout.projectDirectory.file("src/db/db.changelog.xml")
}