package net.astopia.paperconnector.api.module;

import net.astopia.paperconnector.api.PaperConnectorApi;

public interface Module {
    void onEnable();
    void onDisable();
    PaperConnectorApi getPluginApi();
    ModuleConfig getConfig();
}
