import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.withType

plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    //implementation(project(":minecraft:paper-plugin:gui-system-plugin:gui-system-plugin-api"))
    compileOnlyApi("org.incendo:cloud-paper:2.0.0-beta.10")
    compileOnlyApi("org.incendo:cloud-annotations:2.0.0")
    compileOnly(project(":minecraft:paper-connector"))
}