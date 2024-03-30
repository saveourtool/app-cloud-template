import com.saveourtool.diktat.plugin.gradle.DiktatExtension
import com.saveourtool.diktat.plugin.gradle.DiktatGradlePlugin

plugins {
    id("com.saveourtool.template.build.kafka-local-run-configuration")
    id("com.saveourtool.diktat") version "2.0.0" apply false
}

group = "com.saveourtool.template"

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
    }
    apply<DiktatGradlePlugin>()
    configure<DiktatExtension> {
        diktatConfigFile = rootProject.file("diktat-analysis.yml")
        inputs {
            include("src/**/*.kt")
            exclude("src/")
        }
        debug = true
    }
}