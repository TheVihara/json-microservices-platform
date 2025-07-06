package net.unnamed.minecraft.paper.connector.module;

import net.unnamed.minecraft.paper.connector.PaperConnectorPlugin;

public interface Module {
    void onEnable();
    void onDisable();
    PaperConnectorPlugin getPlugin();
    ModuleConfig getConfig();
}
