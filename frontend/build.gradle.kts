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
                implementation(kotlinw("react"))
                implementation(kotlinw("react-dom"))
            }
        }
    }
}
