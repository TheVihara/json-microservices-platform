pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "unnamed-platform"
include("common")
include("service")
include("minecraft")
include("minecraft:paper:paper-connector")
findProject(":minecraft:paper:paper-connector")?.name = "paper-connector"
include("minecraft:velocity-connector")
findProject(":minecraft:velocity-connector")?.name = "velocity-connector"
include("service:common")
findProject(":service:common")?.name = "common"
include("service:party")
findProject(":service:party")?.name = "party"
include("service:friend")
findProject(":service:friend")?.name = "friend"
include("service:permission")
findProject(":service:permission")?.name = "permission"
include("service:punishment")
findProject(":service:punishment")?.name = "punishment"
include("service:auth")
findProject(":service:auth")?.name = "auth"
include("service:deploy")
findProject(":service:deploy")?.name = "deploy"
include("service:player")
findProject(":service:player")?.name = "player"
include("service:command")
findProject(":service:command")?.name = "command"
include("service:command:api")
findProject(":service:command:api")?.name = "api"
include("service:player:api")
findProject(":service:player:api")?.name = "api"
include("minecraft:paper:essentials")
findProject(":minecraft:paper:essentials")?.name = "essentials"
include("minecraft:paper")
findProject(":minecraft:paper")?.name = "paper"
