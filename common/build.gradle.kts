plugins {
    id("java")
}

group = "net.unnamed"
version = "1.0-SNAPSHOT"

dependencies {
    api("io.nats:jnats:2.20.5")
    api("com.alibaba.fastjson2:fastjson2:2.0.57")
}