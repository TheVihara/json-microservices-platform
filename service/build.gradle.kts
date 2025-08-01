subprojects {
    apply(plugin = "java-library")

    dependencies {
        if (project.path != ":service:service-common") {
            "implementation"(project(":service:service-common"))
        }
    }

    tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
        manifest {
            attributes["Main-Class"] = "net.unnamed.service.common.ServiceBootstrapper"
        }
    }
}
