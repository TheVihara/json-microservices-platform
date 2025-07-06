import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":service:command:api"))
    compileOnly(project(":minecraft:paper:paper-connector"))
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("Command")
    archiveClassifier.set("ConnectorModule")
    archiveVersion.set("")
}