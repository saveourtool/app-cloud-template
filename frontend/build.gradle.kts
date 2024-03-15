import com.saveourtool.template.build.kotlinw

plugins {
    id("com.saveourtool.template.build.kotlin-mpp-with-js-configuration")
}

kotlin {
    js {
        binaries.executable()
    }

    sourceSets {
        jsMain {
            dependencies {
                implementation(kotlinw("extensions"))
                implementation(kotlinw("react"))
                implementation(kotlinw("react-dom"))
                implementation(kotlinw("react-router-dom"))
            }
        }
    }
}
