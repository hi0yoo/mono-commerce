plugins {
    kotlin("plugin.spring")
}

apply(plugin = "org.springframework.boot")

dependencies {
    api("org.springframework.boot:spring-boot-starter")
    api("org.springframework.boot:spring-boot-starter-web")
    api("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation(project(":modules:product:product-core"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}