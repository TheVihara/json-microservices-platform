import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(project(":minecraft:paper:paper-connector"))
    compileOnly("me.clip:placeholderapi:2.11.6")
    implementation(project(":service:pack:api"))
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("McContainer")
    archiveClassifier.set("ConnectorModule")
    archiveVersion.set("")
}