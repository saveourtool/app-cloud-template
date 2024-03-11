plugins {
    id("com.saveourtool.template.build.spring-boot-kotlin-configuration")
    id("com.saveourtool.template.build.s3-local-run-configuration")
    id("com.saveourtool.template.build.mysql-local-run-configuration")
}

kotlin {
    jvm()

    sourceSets {
        jvmMain {
            dependencies {
//                implementation("org.springframework.boot:spring-boot-starter-data-jpa")
                implementation("org.springframework.boot:spring-boot-starter-webflux")
                implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")
            }
        }
    }
}

s3LocalRun {
    startupPath = project.layout.projectDirectory.dir("src/jvmMain/resources/s3")
}

mysqlLocalRun {
    liquibaseChangelogPath = project.layout.projectDirectory.file("src/db/db.changelog.xml")
}