import com.saveourtool.template.build.kotlinw

plugins {
    kotlin("multiplatform")
//    id("com.saveourtool.template.build.kotlin-mpp-with-js-configuration")
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
        val jsMain by getting {
            dependencies {
                implementation(project.dependencies.enforcedPlatform(libs.kotlin.wrappers.bom))
                implementation(kotlinw("react"))
                implementation(kotlinw("react-dom"))
            }
        }
    }
}
