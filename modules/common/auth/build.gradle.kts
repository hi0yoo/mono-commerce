plugins {
    kotlin("plugin.spring")
}

apply(plugin = "org.springframework.boot")

dependencies {
    api("org.springframework.boot:spring-boot-starter")
}