import com.saveourtool.malware.detection.build.registerDockerService

plugins {
    id("com.saveourtool.malware.detection.build.spring-boot-kotlin-configuration")
    id("com.saveourtool.malware.detection.build.s3-local-run-configuration")
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

//project.registerS3Service("backend/src/jvmMain/resources/s3")