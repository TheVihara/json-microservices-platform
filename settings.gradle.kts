pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "astopia-platform"

include("common")

// MINECRAFT
include("minecraft")

include("minecraft:paper-connector")
include("minecraft:paper-connector:paper-connector-api")
include("minecraft:paper-connector:paper-connector-module")
include("minecraft:paper-connector:paper-connector-module:container-paper-connector-module")

include("minecraft:velocity-connector")

include("minecraft:paper-plugin:attribute-system-plugin")
include("minecraft:paper-plugin:essentials-plugin")
include("minecraft:paper-plugin:essentials-plugin:essentials-plugin-api")
include("minecraft:paper-plugin:knowledge-system-plugin")
include("minecraft:paper-plugin:item-system-plugin")
include("minecraft:paper-plugin:item-system-plugin:item-system-plugin-api")
include("minecraft:paper-plugin:gui-system-plugin")
include("minecraft:paper-plugin:gui-system-plugin:gui-system-plugin-api")

// SERVICES
include("service")
include("service:service-common")
include("service:command-service")
include("service:command-service:command-service-api")
include("service:pack-service")
include("service:pack-service:pack-service-api")
include("service:player-service")
include("service:player-service:player-service-api")
include("service:gui-service")
include("service:gui-service:gui-service-api")
include("service:permission-service")
include("minecraft:paper-plugin:command-system-plugin")
include("minecraft:paper-plugin:command-system-plugin:command-system-plugin-api")
include("minecraft:velocity-plugin")
include("minecraft:velocity-plugin:velocity-command-system-plugin")
include("minecraft:velocity-connector:velocity-connector-api")