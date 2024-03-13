plugins {
    id("com.saveourtool.template.build.spring-boot-kotlin-configuration")
    id("com.saveourtool.template.build.s3-local-run-configuration")
    id("com.saveourtool.template.build.mysql-local-run-configuration")
    application
}

application {
  mainClass.set("com.saveourtool.template.backend.BackendApplicationKt")
}

kotlin {
    jvm {
        // another workaround:
        // https://stackoverflow.com/questions/69437212/how-to-use-gradle-distribution-plugin-alongside-with-kotlin-multiplatform
        withJava()
    }

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