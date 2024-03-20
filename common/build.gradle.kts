plugins {
    id("com.saveourtool.template.build.kotlin-mpp-with-jvm-configuration")
}

kotlin {
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.serialization.core)
                implementation("org.springframework:spring-web")
                implementation("io.projectreactor:reactor-core")
                implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
            }
        }
    }
}