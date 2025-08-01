plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(project(":common"))
    compileOnlyApi("net.kyori:adventure-api:4.22.0")
}
