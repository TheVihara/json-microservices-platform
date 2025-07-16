import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.withType

plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":service:pack:api"))
    implementation("team.unnamed:creative-api:1.7.3")
    implementation("team.unnamed:creative-serializer-minecraft:1.7.3")
    implementation("team.unnamed:creative-server:1.7.3")
    implementation("com.github.bsommerfeld.jshepherd:core:3.3.1")
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("PackService")
    archiveClassifier.set("")
    archiveVersion.set("")
}