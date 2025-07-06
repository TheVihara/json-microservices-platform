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
    compileOnlyApi("net.kyori:adventure-text-serializer-gson:4.22.0")
}
