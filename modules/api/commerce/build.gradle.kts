apply(plugin = "org.springframework.boot")

dependencies {
    runtimeOnly("com.mysql:mysql-connector-j")

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation(project(":modules:core:core-common"))
    implementation(project(":modules:core:core-order"))

    implementation(project(":modules:infra:infra-common"))
    implementation(project(":modules:infra:infra-order"))
}