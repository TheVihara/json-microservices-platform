plugins {
    id("com.gradleup.shadow") version "9.0.0-beta15"
}

val excludedProjects = listOf("service", "minecraft")

subprojects {
    if (name !in excludedProjects) {
        apply(plugin = "java-library")
        apply(plugin = "maven-publish")
        apply(plugin = "com.gradleup.shadow")

        group = "net.astopia"

        repositories {
            mavenCentral()
            maven { url = uri("https://jitpack.io") }
            maven {
                name = "papermc"
                url = uri("https://repo.papermc.io/repository/maven-public/")
            }
            maven { url = uri("https://repo.extendedclip.com/releases/") }
            maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/") }
        }

        dependencies {
            if (project.path != ":common") {
                "compileOnly"(project(path = ":common", configuration = "shadow"))
            }
            "compileOnly"("org.projectlombok:lombok:1.18.38")
            "annotationProcessor"("org.projectlombok:lombok:1.18.38")
        }



        tasks.named("build") {
            dependsOn("shadowJar")
        }
    }
}
