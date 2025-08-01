package net.astopia.paperconnector.api.module;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.astopia.paperconnector.api.PaperConnectorApi;
import net.unnamed.common.database.mysql.MySqlDatabase;

import java.nio.file.Path;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Setter
@Getter
public abstract class ConnectorModule implements Module {
    PaperConnectorApi pluginApi;
    Path dataFolder;
    ModuleConfig config;
    MySqlDatabase mySqlDatabase;

    @Override
    public ModuleConfig getConfig() {
        return config;
    }

    @Override
    public PaperConnectorApi getPluginApi() {
        return pluginApi;
    }

    public void setPluginApi(PaperConnectorApi pluginApi) {
        this.pluginApi = pluginApi;
        dataFolder = pluginApi.getPlugin().getDataFolder().toPath().resolve("modules").resolve(config.getId().toLowerCase());
        dataFolder.toFile().mkdirs();
        mySqlDatabase = pluginApi.getMySqlDatabase();
    }
}
