repositories {
    mavenCentral()
    gradlePluginPortal()
}

plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    jvm {

    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
