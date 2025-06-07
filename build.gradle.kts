plugins {
    id("com.gradleup.shadow") version "9.0.0-beta15"
}

val excludedProjects = listOf("service", "minecraft")

subprojects {
    if (name !in excludedProjects) {
        apply(plugin = "java-library")
        apply(plugin = "maven-publish")

        dependencies {
            "implementation"(project(":common"))
            "implementation"("com.github.bsommerfeld:jshepherd:3.2.0")
        }
    }

    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

tasks.withType<JavaCompile>().configureEach {
    enabled = false
}