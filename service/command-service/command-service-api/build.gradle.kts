plugins {
    id("java")
}

group = "net.astopia"

repositories {
    mavenCentral()
}

dependencies {
    api("org.incendo:cloud-core:2.0.0")
    api("org.incendo:cloud-annotations:2.0.0")
}
