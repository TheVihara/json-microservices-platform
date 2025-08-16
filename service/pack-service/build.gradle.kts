import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
}

dependencies {
    implementation(project(":service:pack-service:pack-service-api"))
    implementation(project(":service:command-service:command-service-api"))
    implementation("team.unnamed:creative-api:1.8.2-SNAPSHOT")
    implementation("team.unnamed:creative-serializer-minecraft:1.8.2-SNAPSHOT")
    implementation("team.unnamed:creative-server:1.8.2-SNAPSHOT")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.5.4")
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("PackService")
    archiveClassifier.set("")
    archiveVersion.set("")
}