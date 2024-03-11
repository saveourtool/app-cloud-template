package com.saveourtool.template.build

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
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