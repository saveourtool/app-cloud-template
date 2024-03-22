package com.saveourtool.template.build

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.the

fun kotlinw(target: String): String = "org.jetbrains.kotlin-wrappers:kotlin-$target"

internal fun addAllSpringRelatedBoms(
    project: Project,
    implementation: (Provider<MinimalExternalModuleDependency>) -> Dependency?,
) {
    @Suppress("GENERIC_VARIABLE_WRONG_DECLARATION")
    val libs = project.the<LibrariesForLibs>()
    implementation(project.dependencies.enforcedPlatform(libs.spring.boot.bom))
    implementation(project.dependencies.platform(libs.spring.cloud.bom))
    implementation(project.dependencies.platform(libs.spring.data.bom))
    implementation(project.dependencies.platform(libs.springdoc.openapi.bom))
}