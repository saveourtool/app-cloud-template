package com.saveourtool.template.build

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.*

plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()

    sourceSets {
        jvmMain {
            dependencies {
                addAllSpringRelatedBoms(project, ::implementation)
            }
        }
    }
}

configureKotlinCompile()

tasks.withType<Test> {
    useJUnitPlatform()
}
