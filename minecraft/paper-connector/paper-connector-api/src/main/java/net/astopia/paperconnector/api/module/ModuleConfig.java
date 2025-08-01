package net.astopia.paperconnector.api.module;

public interface ModuleConfig {
    String getId();
    String getVersion();
    String[] getDependencies();
}
