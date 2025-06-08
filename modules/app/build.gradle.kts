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

    implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.tngtech.archunit:archunit:1.2.1")
    testImplementation("com.tngtech.archunit:archunit-junit5:1.2.1")
}