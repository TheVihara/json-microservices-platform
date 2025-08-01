import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.withType

subprojects {
    dependencies {
        compileOnlyApi("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
        annotationProcessor("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
        compileOnlyApi("net.kyori:adventure-api:4.22.0")
    }

    if (!name.endsWith("-api")) {
        tasks.withType<ShadowJar> {
            destinationDirectory.set(file("$rootDir/velocity-plugins"))
        }
    }
}