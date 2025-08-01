import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.withType

plugins {
    id("java")
}

dependencies {
    implementation(project(":minecraft:velocity-connector:velocity-connector-api"))
    implementation(project(":common"))

    annotationProcessor("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("VelocityConnector")
    archiveClassifier.set("AstopiaVelocityPlugin")
    archiveVersion.set("")
    destinationDirectory.set(file("$rootDir/velocity-plugins"))
}