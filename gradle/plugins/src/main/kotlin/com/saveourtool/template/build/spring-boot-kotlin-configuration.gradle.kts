package com.saveourtool.template.build

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    kotlin("plugin.serialization")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    // • First project (Spring Boot 3.1.2, Gradle 8.1.1) was fixed by adding id("application") as first plugin. @nbam-e 's idea about moving Kotlin before Spring Boot also worked for me.
    // • Second project (Spring Boot 2.7.14, Gradle 7.6.1) was fixed with same way as first project (by adding id("application")).
    // • Third project (Spring Boot 2.7.14, Gradle 8.1.1) didn't work at all.
    // Workaround from https://github.com/springdoc/springdoc-openapi-gradle-plugin/issues/121
    application
}

@Suppress("GENERIC_VARIABLE_WRONG_DECLARATION")
val libs = the<LibrariesForLibs>()
val javaVersion: JavaVersion = JavaVersion.toVersion(libs.versions.java.get())

java {
    sourceCompatibility = javaVersion
}

kotlin {
    jvmToolchain {
        this.languageVersion.set(JavaLanguageVersion.of(javaVersion.majorVersion))
    }

    sourceSets {
        jvmMain {
            dependencies {
                implementation(libs.springdoc.openapi.starter.common)
                implementation(libs.springdoc.openapi.starter.webflux.ui)
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = javaVersion.majorVersion
    }
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
