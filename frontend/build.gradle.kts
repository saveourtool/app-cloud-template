import com.saveourtool.template.build.kotlinw

plugins {
    kotlin("multiplatform")
}

kotlin {
    js {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled = true
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        jsMain {
            dependencies {
                implementation(project.dependencies.enforcedPlatform(libs.kotlin.wrappers.bom))
                implementation(kotlinw("react"))
                implementation(kotlinw("react-dom"))
            }
        }
    }
}
