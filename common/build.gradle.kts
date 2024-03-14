plugins {
    id("com.saveourtool.template.build.kotlin-mpp-configuration")
}

kotlin {
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core")
            }
        }
    }
}