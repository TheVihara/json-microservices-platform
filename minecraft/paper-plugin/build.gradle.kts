import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.withType

subprojects {
    dependencies {
        compileOnlyApi("io.papermc.paper:paper-api:1.21.6-R0.1-SNAPSHOT")
        compileOnlyApi("net.kyori:adventure-api:4.22.0")
    }

    if (!name.endsWith("-api")) {
        tasks.withType<ShadowJar> {
            destinationDirectory.set(file("$rootDir/paper-plugins"))
        }
    }
}