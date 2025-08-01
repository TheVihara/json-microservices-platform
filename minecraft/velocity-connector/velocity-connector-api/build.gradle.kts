import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.withType

plugins {
    id("java")
}

dependencies {
    compileOnlyApi(project(":common"))
    compileOnlyApi("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    compileOnlyApi("net.kyori:adventure-api:4.22.0")
}