plugins {
    id("com.saveourtool.template.build.spring-boot-kotlin-configuration")
}

dependencies {
    implementation(projects.common)
    implementation(projects.authenticationUtils)
    implementation(libs.springdoc.openapi.starter.common)
    implementation(libs.springdoc.openapi.starter.webmvc.ui)
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
}