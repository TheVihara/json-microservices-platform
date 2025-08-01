package net.astopia.paperconnector.api.module;

import java.io.File;

public class ModuleDescriptor {
    private final String id;
    private final String mainClass;
    private final String version;
    private final String[] dependencies;
    private final File jarFile;

    public ModuleDescriptor(String id, String mainClass, String version, String[] dependencies, File jarFile) {
        this.id = id;
        this.mainClass = mainClass;
        this.version = version;
        this.dependencies = dependencies;
        this.jarFile = jarFile;
    }

    public String getId() { return id; }
    public String getMainClass() { return mainClass; }
    public String getVersion() { return version; }
    public String[] getDependencies() { return dependencies; }
    public File getJarFile() { return jarFile; }

}
