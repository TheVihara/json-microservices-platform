plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    api("org.jline:jline:3.21.0")
    api(project(":common"))
}
