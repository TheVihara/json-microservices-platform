package net.unnamed.minecraft.paper.connector;

import de.bsommerfeld.jshepherd.core.ConfigurationLoader;
import net.unnamed.common.ClassInstance;
import net.unnamed.common.config.CustomYamlPersistenceDelegateFactory;
import net.unnamed.common.logging.PlatformLogger;
import net.unnamed.common.nats.NatsManager;
import net.unnamed.common.packet.PacketRegistry;
import net.unnamed.minecraft.paper.connector.config.ConnectorConfig;
import net.unnamed.minecraft.paper.connector.module.ModuleManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PaperConnectorPlugin extends JavaPlugin implements ClassInstance {
    private final CustomYamlPersistenceDelegateFactory persistenceDelegateFactory = new CustomYamlPersistenceDelegateFactory();
    private final PlatformLogger logger = new PlatformLogger("PaperConnector");
    private final ModuleManager moduleManager = new ModuleManager(this);
    private final PacketRegistry packetRegistry = new PacketRegistry();
    private final ConnectorConfig connectorConfig = ConfigurationLoader.load(getDataFolder().toPath().resolve("config.yml"), ConnectorConfig::new);
    private final NatsManager natsManager = NatsManager.INSTANCE;

    @Override
    public void onLoad() {
        //natsManager.init();
        moduleManager.init(this);
    }

    @Override
    public void onEnable() {
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