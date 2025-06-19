plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":service:player:api"))
}
