plugins {
    id("com.saveourtool.template.build.spring-boot-kotlin-configuration")
    id("com.saveourtool.template.build.s3-local-run-configuration")
    id("com.saveourtool.template.build.mysql-local-run-configuration")
}

dependencies {
    implementation(libs.springdoc.openapi.starter.common)
    implementation(libs.springdoc.openapi.starter.webmvc.ui)
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")
    implementation(projects.common)
}

s3LocalRun {
    startupPath = project.layout.projectDirectory.dir("src/main/resources/s3")
}

mysqlLocalRun {
    databaseName = "backend_mvc"
    liquibaseChangelogPath = project.layout.projectDirectory.file("src/db/db.changelog.xml")
}