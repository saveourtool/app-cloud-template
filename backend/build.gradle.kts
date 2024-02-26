plugins {
    id("com.saveourtool.malware.detection.build.spring-boot-kotlin-configuration")
}

kotlin {
    jvm()

    sourceSets {
        jvmMain {
            dependencies {
                implementation("org.springframework.boot:spring-boot-starter-data-jpa")
                implementation("org.springframework.boot:spring-boot-starter-webflux")
            }
        }
    }
}