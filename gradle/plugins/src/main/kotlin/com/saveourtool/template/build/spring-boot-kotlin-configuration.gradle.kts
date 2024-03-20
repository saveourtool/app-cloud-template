package com.saveourtool.template.build

import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.*

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    kotlin("plugin.serialization")
    id("org.springframework.boot")
}

configureKotlinCompile()

dependencies {
    addAllSpringRelatedBoms(project, ::implementation)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
