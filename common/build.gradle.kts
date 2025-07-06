plugins {
    id("java")
}

group = "net.unnamed"
version = "1.0-SNAPSHOT"

dependencies {
    api("io.nats:jnats:2.20.5")
    api("com.alibaba.fastjson2:fastjson2:2.0.57")
    api("com.zaxxer:HikariCP:6.3.0")
    implementation("com.google.auto.service:auto-service-annotations:1.1.1")
    annotationProcessor("com.google.auto.service:auto-service:1.1.1")
}