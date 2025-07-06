plugins {
    id("com.gradleup.shadow") version "9.0.0-beta15"
}

val excludedProjects = listOf("service", "minecraft")

subprojects {
    if (name !in excludedProjects) {
        apply(plugin = "java-library")
        apply(plugin = "maven-publish")
        apply(plugin = "com.gradleup.shadow")

        repositories {
            mavenCentral()
            maven { url = uri("https://jitpack.io") }
            maven {
                name = "papermc"
                url = uri("https://repo.papermc.io/repository/maven-public/")
            }
        }

        dependencies {
            if (project.path != ":common") {
                "implementation"(project(":common"))
            }
            "compileOnly"("com.github.bsommerfeld.jshepherd:core:3.3.1")
            "compileOnly"("com.github.bsommerfeld.jshepherd:yaml:3.3.1")
            "implementation"("org.yaml:snakeyaml:2.4")
            "implementation"("org.capnproto:runtime:0.1.16")
        }



        tasks.named("build") {
            dependsOn("shadowJar")
        }
    }
}
