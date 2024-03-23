import org.ajoberstar.reckon.core.Reckoner
import org.ajoberstar.reckon.core.Scope
import org.ajoberstar.reckon.gradle.ReckonExtension

rootProject.name = "app-cloud-template"

// ==== gradle configuration =============
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

// ==== dependencies =====================
pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

plugins {
    id("org.ajoberstar.reckon.settings") version "0.18.3"
}

// ==== automated configuration of version =
extensions.configure<ReckonExtension> {
    setDefaultInferredScope(Scope.PATCH)
    stages("beta", "rc", Reckoner.FINAL_STAGE)
    setScopeCalc(calcScopeFromProp().or(calcScopeFromCommitMessages()))
    setStageCalc(calcStageFromProp())
}

// ==== subprojects ========================
includeBuild("gradle/plugins")
include("common")
include("backend-webmvc")
include("backend-webflux")
include("frontend")
