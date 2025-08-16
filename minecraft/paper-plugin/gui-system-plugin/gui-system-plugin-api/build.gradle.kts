plugins {
    id("java")
}

dependencies {
    api(project(":service:gui-service:gui-service-api")) {
        exclude("net.astopia", "common")
        exclude("net.astopia", "service-common")
        exclude("net.astopia", "command-service-api")
        exclude("net.astopia", "player-service-api")
    }
    compileOnlyApi(project(":service:player-service:player-service-api")) {
        exclude("net.astopia", "common")
        exclude("net.astopia", "service-common")
    }
    api(project(":service:pack-service:pack-service-api")) {
        exclude("net.astopia", "common")
        exclude("net.astopia", "service-common")
    }
}