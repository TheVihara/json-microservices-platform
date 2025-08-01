import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
}

dependencies {
    implementation(project(":service:command-service:command-service-api")) {
        exclude("org.incendo", "cloud-core")
        exclude("net.astopia", "common")
    }
    implementation("org.incendo:cloud-velocity:2.0.0-beta.10")
    compileOnly(project(":minecraft:velocity-connector:velocity-connector-api")) {
        exclude("net.astopia", "common")
    }
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("CommandSystem")
    archiveClassifier.set("AstopiaVelocityPlugin")
    archiveVersion.set("")
}