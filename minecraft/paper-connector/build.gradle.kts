import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
}

dependencies {
    implementation(project(path = ":common", configuration = "shadow"))
    api(project(":minecraft:paper-connector:paper-connector-api")) {
        exclude(group = "com.fasterxml.jackson.core")
        exclude(group = "com.fasterxml.jackson.dataformat")
        exclude(group = "com.fasterxml.jackson.databind")
    }
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("PaperConnector")
    archiveClassifier.set("AstopiaPlugin")
    archiveVersion.set("")
    destinationDirectory.set(file("$rootDir/plugins"))
}