import org.ajoberstar.reckon.core.Reckoner
import org.ajoberstar.reckon.core.Scope
import org.ajoberstar.reckon.gradle.ReckonExtension

rootProject.name = "app-cloud-template"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("com.gradle.enterprise") version "3.15.1"
    id("org.ajoberstar.reckon.settings") version "0.18.3"
}

includeBuild("gradle/plugins")
include("backend-webmvc")
include("backend-webflux")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

gradleEnterprise {
    @Suppress("AVOID_NULL_CHECKS")
    if (System.getenv("CI") != null) {
        buildScan {
            publishAlways()
            termsOfServiceUrl = "https://gradle.com/terms-of-service"
            termsOfServiceAgree = "yes"
        }
    }
}

extensions.configure<ReckonExtension> {
    setDefaultInferredScope(Scope.PATCH)
    stages("beta", "rc", Reckoner.FINAL_STAGE)
    setScopeCalc(calcScopeFromProp().or(calcScopeFromCommitMessages()))
    setStageCalc(calcStageFromProp())
}
