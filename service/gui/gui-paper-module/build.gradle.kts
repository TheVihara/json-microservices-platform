plugins {
    id("java")
}

group = "net.unnamed.service.gui.module"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":service:gui:api"))
    compileOnly(project(":minecraft:paper:paper-connector"))
}