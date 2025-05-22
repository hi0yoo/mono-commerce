apply(plugin = "org.springframework.boot")
apply(plugin = "org.jetbrains.kotlin.plugin.jpa")

dependencies {
    testImplementation("com.tngtech.archunit:archunit:1.2.1")
    // JUnit5 사용 시
    testImplementation("com.tngtech.archunit:archunit-junit5:1.2.1")

    // 분석 대상 모듈 추가
    implementation(project(":modules:core:core-common"))
    implementation(project(":modules:core:core-order"))
    implementation(project(":modules:infra:infra-common"))
    implementation(project(":modules:infra:infra-order"))
}
