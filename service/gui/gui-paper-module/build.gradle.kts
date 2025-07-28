import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.withType

plugins {
    id("java")
}

group = "net.unnamed.service.gui.module"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":service:gui:api"))
    compileOnly(project(":minecraft:paper:paper-connector"))
    implementation(project(":service:pack:api"))
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("GuiSystem")
    archiveClassifier.set("ConnectorModule")
    archiveVersion.set("")
}