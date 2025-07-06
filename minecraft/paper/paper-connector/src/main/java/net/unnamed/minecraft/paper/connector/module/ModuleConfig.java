package net.unnamed.minecraft.paper.connector.module;

public interface ModuleConfig {
    String getId();
    String getVersion();
    String[] getDependencies();
}
