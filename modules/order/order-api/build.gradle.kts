plugins {
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
}

apply(plugin = "org.springframework.boot")
apply(plugin = "org.jetbrains.kotlin.plugin.jpa")

dependencies {
    api("org.springframework.boot:spring-boot-starter")
    api("org.springframework.boot:spring-boot-starter-web")
    api("com.fasterxml.jackson.module:jackson-module-kotlin")

    api("org.springframework.boot:spring-boot-starter-data-jpa")

    api("org.springframework.boot:spring-boot-starter-data-redis")

    implementation(project(":modules:common:snowflake"))
    implementation(project(":modules:common:auth"))
    implementation(project(":modules:product:product-core"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}