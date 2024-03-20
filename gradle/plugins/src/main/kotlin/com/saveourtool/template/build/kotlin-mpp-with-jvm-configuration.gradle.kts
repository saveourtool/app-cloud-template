package com.saveourtool.template.build

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.*

plugins {
    kotlin("multiplatform")
}

@Suppress("GENERIC_VARIABLE_WRONG_DECLARATION")
val libs = the<LibrariesForLibs>()

kotlin {
    jvm()

    sourceSets {
        jvmMain {
            dependencies {
                implementation(project.dependencies.enforcedPlatform(libs.spring.boot.dependencies))
            }
        }
    }
}

configureKotlinCompile()

tasks.withType<Test> {
    useJUnitPlatform()
}
