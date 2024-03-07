plugins {
    id("com.saveourtool.template.build.spring-boot-kotlin-configuration")
    id("com.saveourtool.template.build.s3-local-run-configuration")
}

kotlin {
    jvm()

    sourceSets {
        jvmMain {
            dependencies {
//                implementation("org.springframework.boot:spring-boot-starter-data-jpa")
                implementation("org.springframework.boot:spring-boot-starter-webflux")
            }
        }
    }
}

s3LocalRun {
    startupPath = "backend/src/jvmMain/resources/s3"
}