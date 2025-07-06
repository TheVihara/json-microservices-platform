import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.withType

plugins {
    id("java")
}

dependencies {
    implementation("com.github.bsommerfeld.jshepherd:core:3.3.1")
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("PaperConnector")
    archiveClassifier.set("AstopiaPlugin")
    archiveVersion.set("")
}