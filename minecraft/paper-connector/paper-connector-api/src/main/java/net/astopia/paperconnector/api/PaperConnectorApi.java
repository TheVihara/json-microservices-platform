package net.astopia.paperconnector.api;

import net.astopia.paperconnector.api.config.ConnectorConfig;
import net.astopia.paperconnector.api.module.ModuleApi;
import net.unnamed.common.database.mysql.MySqlDatabase;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public interface PaperConnectorApi {
    JavaPlugin getPlugin();
    MySqlDatabase getMySqlDatabase();
    ConnectorConfig getConnectorConfig();
    ModuleApi getModuleApi();

    static PaperConnectorApi getInstance() {
        return Bukkit.getServicesManager().load(PaperConnectorApi.class);
    }
}
