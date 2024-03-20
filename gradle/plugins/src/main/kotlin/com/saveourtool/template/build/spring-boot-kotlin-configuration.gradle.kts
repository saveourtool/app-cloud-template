package com.saveourtool.template.build

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    kotlin("plugin.serialization")
    id("org.springframework.boot")
}

configureKotlinCompile()

@Suppress("GENERIC_VARIABLE_WRONG_DECLARATION")
val libs = the<LibrariesForLibs>()
dependencies {
    implementation(project.dependencies.enforcedPlatform(libs.spring.boot.dependencies))
    implementation(project.dependencies.platform(libs.spring.cloud.dependencies))
}

tasks.withType<Test> {
    useJUnitPlatform()
}
