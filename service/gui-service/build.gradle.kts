plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":service:gui-service:gui-service-api"))
}
