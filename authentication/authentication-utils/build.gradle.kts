plugins {
    id("com.saveourtool.template.build.spring-boot-kotlin-configuration")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation(libs.kotlin.logging)
}