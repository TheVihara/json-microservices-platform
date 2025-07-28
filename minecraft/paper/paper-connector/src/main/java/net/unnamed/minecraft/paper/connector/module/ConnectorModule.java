package net.unnamed.minecraft.paper.connector.module;

import net.unnamed.common.database.mysql.MySqlDatabase;
import net.unnamed.minecraft.paper.connector.PaperConnectorPlugin;

import java.nio.file.Path;

public abstract class ConnectorModule implements Module {
    protected PaperConnectorPlugin plugin;
    protected Path dataFolder;
    protected ModuleConfig config;
    protected MySqlDatabase mySqlDatabase;

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
        dataFolder = plugin.getDataFolder().toPath().resolve("modules").resolve(config.getId().toLowerCase());
        dataFolder.toFile().mkdirs();
        mySqlDatabase = plugin.getMySqlDatabase();
    }
}
