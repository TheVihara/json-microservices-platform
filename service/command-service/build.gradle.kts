import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
}

dependencies {
    implementation(project(":service:command-service:command-service-api"))
    implementation("org.mariadb.jdbc:mariadb-java-client:3.5.4")
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("CommandService")
    archiveClassifier.set("")
    archiveVersion.set("")
}