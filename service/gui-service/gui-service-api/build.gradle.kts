plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":service:command-service:command-service-api"))
    api(project(":service:player-service:player-service-api"))
    compileOnlyApi("net.kyori:adventure-api:4.22.0")
    compileOnlyApi("net.kyori:adventure-text-serializer-gson:4.22.0")
    compileOnlyApi("org.apache.commons:commons-lang3:3.18.0")
}
