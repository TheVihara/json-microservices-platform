package net.unnamed.minecraft.paper.connector;

import de.bsommerfeld.jshepherd.core.ConfigurationLoader;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.unnamed.common.ClassInstance;
import net.unnamed.common.config.CustomYamlPersistenceDelegateFactory;
import net.unnamed.common.database.mysql.MySqlDatabase;
import net.unnamed.common.logging.PlatformLogger;
import net.unnamed.common.nats.NatsManager;
import net.unnamed.common.packet.PacketRegistry;
import net.unnamed.minecraft.paper.connector.config.ConnectorConfig;
import net.unnamed.minecraft.paper.connector.module.ModuleManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Getter
public class PaperConnectorPlugin extends JavaPlugin implements ClassInstance {
    CustomYamlPersistenceDelegateFactory persistenceDelegateFactory = new CustomYamlPersistenceDelegateFactory();
    PlatformLogger logger = new PlatformLogger("PaperConnector");
    PacketRegistry packetRegistry = new PacketRegistry();
    ConnectorConfig connectorConfig = ConfigurationLoader.load(getDataFolder().toPath().resolve("config.yml"), ConnectorConfig::new);
    MySqlDatabase mySqlDatabase = new MySqlDatabase(connectorConfig.getMySqlConfig());
    ModuleManager moduleManager = new ModuleManager(this);
    NatsManager natsManager = NatsManager.INSTANCE;

    @Override
    public void onLoad() {
        //natsManager.init();
    }

    @Override
    public void onEnable() {
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

    public PacketRegistry getPacketRegistry() {
        return packetRegistry;
    }
}