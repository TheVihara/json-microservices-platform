import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
}

repositories {
    maven { url = uri("https://repo.extendedclip.com/releases/") }
    maven { url = uri("https://maven.citizensnpcs.co/repo") }
}

dependencies {
    compileOnly(project(":minecraft:paper-plugin:command-system-plugin:command-system-plugin-api"))
    compileOnly(project(":minecraft:paper-plugin:gui-system-plugin:gui-system-plugin-api"))
    compileOnly(project(":minecraft:paper-plugin:essentials-plugin:essentials-plugin-api"))
    compileOnly(project(":minecraft:paper-plugin:item-system-plugin:item-system-plugin-api"))
    compileOnly(project(":minecraft:paper-connector:paper-connector-api"))
    compileOnlyApi("me.clip:placeholderapi:2.11.6")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7") {
        exclude(group = "org.bukkit", module = "bukkit")
    }
    implementation("com.google.guava:guava:32.1.3-jre")
    compileOnly("net.citizensnpcs:citizens-main:2.0.35-SNAPSHOT") {
        exclude(group = "*", module = "*")
    }
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("AnimationSystem")
    archiveClassifier.set("AstopiaPlugin")
    archiveVersion.set("")
}