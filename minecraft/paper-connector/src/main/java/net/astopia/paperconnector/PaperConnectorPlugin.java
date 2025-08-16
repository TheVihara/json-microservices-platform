package net.astopia.paperconnector;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.astopia.paperconnector.api.PaperConnectorApi;
import net.astopia.paperconnector.api.config.ConnectorConfig;
import net.astopia.paperconnector.api.module.ModuleApi;
import net.astopia.paperconnector.module.ModuleManager;
import net.unnamed.common.ClassInstance;
import net.unnamed.common.config.YamlConfig;
import net.unnamed.common.database.mysql.MySqlDatabase;
import net.unnamed.common.logging.PlatformLogger;
import net.unnamed.common.nats.NatsManager;
import net.unnamed.common.packet.PacketRegistry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Getter
public class PaperConnectorPlugin extends JavaPlugin implements ClassInstance, PaperConnectorApi {
    PlatformLogger logger = new PlatformLogger("PaperConnector");
    PacketRegistry packetRegistry = new PacketRegistry();
    ConnectorConfig connectorConfig = YamlConfig.loadSafe(ConnectorConfig.class, getDataFolder().toPath().resolve("config.yml"), ConnectorConfig::new);
    MySqlDatabase mySqlDatabase = new MySqlDatabase(connectorConfig.getMySqlConfig());
    ModuleManager moduleManager = new ModuleManager(this);
    NatsManager natsManager = NatsManager.INSTANCE;

    @Override
    public void onLoad() {
        //natsManager.init();
    }

    @Override
    public void onEnable() {
        Bukkit.getServicesManager().register(PaperConnectorApi.class, this, this, ServicePriority.Normal);
        moduleManager.init(this);
        moduleManager.enable(this);
    }

    @Override
    public void onDisable() {
        moduleManager.disableAllModules();
        //natsManager.shutdown();
    }

    @Override
    public @NotNull PlatformLogger getLogger() {
        return logger;
    }

    @Override
    public ModuleApi getModuleApi() {
        return moduleManager;
    }

    @Override
    public JavaPlugin getPlugin() {
        return this;
    }
}