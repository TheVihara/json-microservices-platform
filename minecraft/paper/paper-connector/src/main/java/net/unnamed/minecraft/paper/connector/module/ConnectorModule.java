package net.unnamed.minecraft.paper.connector.module;

import net.unnamed.minecraft.paper.connector.PaperConnectorPlugin;

public abstract class ConnectorModule implements Module {
    protected PaperConnectorPlugin plugin;
    protected ModuleConfig config;

    @Override
    public ModuleConfig getConfig() {
        return config;
    }

    @Override
    public PaperConnectorPlugin getPlugin() {
        return plugin;
    }

    public void setConfig(ModuleConfig config) {
        this.config = config;
    }

    public void setPlugin(PaperConnectorPlugin plugin) {
        this.plugin = plugin;
    }
}
