subprojects {
    if (project.name != ":paper-plugin" || project.name != ":velocity-plugin") {
        apply(plugin = "java-library")
    }
}