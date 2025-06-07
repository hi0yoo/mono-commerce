plugins {
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    kotlin("kapt")
}

apply(plugin = "org.springframework.boot")
apply(plugin = "org.jetbrains.kotlin.plugin.jpa")

dependencies {
    api("org.springframework.boot:spring-boot-starter")

    api("org.springframework.boot:spring-boot-starter-data-jpa")

    api("com.querydsl:querydsl-jpa:5.1.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.1.0:jakarta")
    kapt("jakarta.annotation:jakarta.annotation-api")
    kapt("jakarta.persistence:jakarta.persistence-api")

    implementation(project(":modules:common:snowflake"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.postgresql:postgresql")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.3")
}