plugins {
    id("java")
}

group = "net.unnamed.minecraft.paper.plugin.guisystem"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    compileOnlyApi("io.papermc.paper:paper-api:1.21.6-R0.1-SNAPSHOT")
    compileOnlyApi("net.kyori:adventure-api:4.22.0")
}
