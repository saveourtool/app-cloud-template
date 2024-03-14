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
        useCommonJs()
        binaries.executable()
    }

    sourceSets {
        jsMain {
            dependencies {
                implementation(project.dependencies.platform(libs.kotlin.wrappers.bom))
                implementation(kotlinw("extensions"))
                implementation(kotlinw("react"))
                implementation(kotlinw("react-dom"))
            }
        }
    }
}

fun kotlinw(target: String): String = "org.jetbrains.kotlin-wrappers:kotlin-$target"