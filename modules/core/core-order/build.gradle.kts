apply(plugin = "org.springframework.boot")
apply(plugin = "org.jetbrains.kotlin.plugin.jpa")

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation(project(":modules:core:core-common"))
}