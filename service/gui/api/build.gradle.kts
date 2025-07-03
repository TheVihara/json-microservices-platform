plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":service:command:api"))
    api(project(":service:player:api"))
    compileOnlyApi("net.kyori:adventure-api:4.22.0")
}
