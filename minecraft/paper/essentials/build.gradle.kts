import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
}

repositories {
    maven { url = uri("https://repo.extendedclip.com/releases/") }
}

dependencies {
    implementation(project(":service:player:api"))
    compileOnlyApi("me.clip:placeholderapi:2.11.6")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7") {
        exclude(group = "org.bukkit", module = "bukkit")
    }
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("Essentials")
    archiveClassifier.set("AstopiaPlugin")
    archiveVersion.set("")
}