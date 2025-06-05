plugins {
    kotlin("plugin.spring")
}

apply(plugin = "org.springframework.boot")

dependencies {
    implementation(project(":modules:product"))
    implementation(project(":modules:product:product-core"))
    implementation(project(":modules:product:product-api"))
    implementation(project(":modules:order"))
    implementation(project(":modules:order:order-api"))

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")

    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}