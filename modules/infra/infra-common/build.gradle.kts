apply(plugin = "org.springframework.boot")

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")

    implementation(project(":modules:core:core-common"))
}