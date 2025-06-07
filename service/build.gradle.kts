subprojects {
    apply(plugin = "java-library")

    dependencies {
        "implementation"(project(":service:common"))
    }

    tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
        manifest {
            attributes["Main-Class"] = "net.unnamed.service.common.ServiceBootstrapper"
        }
    }
}
