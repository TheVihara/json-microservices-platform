plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":common"))
    compileOnlyApi("net.kyori:adventure-api:4.22.0")
}
