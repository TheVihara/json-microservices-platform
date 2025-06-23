subprojects {
    if (project.name != ":paper") {
        apply(plugin = "java-library")
    }
}