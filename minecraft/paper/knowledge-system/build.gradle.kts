import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
}

repositories {
    maven { url = uri("https://repo.extendedclip.com/releases/") }
}

dependencies {
    compileOnly(project(":minecraft:paper:essentials:api"))
    compileOnly(project(":service:gui:gui-paper-module"))
    compileOnlyApi("me.clip:placeholderapi:2.11.6")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7") {
        exclude(group = "org.bukkit", module = "bukkit")
    }
    implementation("com.google.guava:guava:32.1.3-jre")
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("KnowledgeSystem")
    archiveClassifier.set("AstopiaPlugin")
    archiveVersion.set("")
}