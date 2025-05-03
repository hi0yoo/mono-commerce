apply(plugin = "org.springframework.boot")
apply(plugin = "org.jetbrains.kotlin.plugin.jpa")

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.1.0:jakarta")
    kapt("jakarta.annotation:jakarta.annotation-api")
}
