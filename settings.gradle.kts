rootProject.name = "unnamed-platform"
include("common")
include("service")
include("minecraft")
include("minecraft:bukkit")
findProject(":minecraft:bukkit")?.name = "bukkit"
include("minecraft:velocity")
findProject(":minecraft:velocity")?.name = "velocity"
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
