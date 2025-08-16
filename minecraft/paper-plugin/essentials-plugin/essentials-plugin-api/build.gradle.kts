plugins {
    id("java")
}

dependencies {
    api(project(":service:player-service:player-service-api")) {
        exclude("net.astopia", "common")
        exclude("net.astopia", "service-common")
    }
}

