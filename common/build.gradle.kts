import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
}

dependencies {
    api("io.nats:jnats:2.20.5")
    api("com.alibaba.fastjson2:fastjson2:2.0.57")
    api("com.zaxxer:HikariCP:6.3.0")

    api("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.18.0")
}

tasks.withType<ShadowJar> {
    relocate("com.fasterxml.jackson", "net.astopia.jackson")

    archiveClassifier.set("")
}

tasks.jar {
    enabled = false
    dependsOn(tasks.shadowJar)
}

configurations {
    apiElements {
        outgoing.artifacts.clear()
        outgoing.artifact(tasks.shadowJar)
    }
    runtimeElements {
        outgoing.artifacts.clear()
        outgoing.artifact(tasks.shadowJar)
    }
}