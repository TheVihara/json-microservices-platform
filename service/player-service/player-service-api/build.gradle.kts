plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":service:command-service:command-service-api"))
    compileOnlyApi("net.kyori:adventure-api:4.22.0")
}
