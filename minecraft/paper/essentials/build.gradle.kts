plugins {
    id("java")
}

dependencies {
    implementation(project(":service:player:api"))
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
}