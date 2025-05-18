plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    kotlin("kapt") version "1.9.25" // for querydsl
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "me.hi0yoo"
version = "0.0.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

allprojects {
    repositories {
        mavenCentral()
    }

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.kapt")

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    // for querydsl
    val generated = file("src/main/generated")
    tasks.withType<JavaCompile> {
        options.generatedSourceOutputDirectory.set(generated)
    }

    sourceSets {
        main {
            kotlin.srcDirs += generated
        }
    }

    tasks.named("clean") {
        doLast {
            generated.deleteRecursively()
        }
    }

    kapt {
        generateStubs = true
    }

    plugins.withId("org.springframework.boot") {
        // 부트 모듈이 아닌 경우 mainClass 에러 방지
        if (project.name != "commerce") {
            tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
                enabled = false
            }
        }
    }
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.named("bootJar") {
    dependsOn("copyCommerceJar")
}

tasks.register<Copy>("copyCommerceJar") {
    dependsOn(":modules:api:commerce:bootJar")

    val jarFile = file("modules/api/commerce/build/libs/commerce.jar")
    val targetDir = file("$buildDir/libs")

    from(jarFile)
    into(targetDir)

    doFirst {
        println("📦 Copying ${jarFile.name} → $targetDir")
    }
}

tasks.register<Exec>("dockerBuild") {
    group = "docker"
    description = "Build bootJar and then Docker image"

    executable = "/opt/homebrew/bin/docker"
    args("build", "-t", "commerce-app:latest", ".")
}