package net.astopia.velocityconnector;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.astopia.velocityconnector.api.VelocityConnectorApi;
import net.unnamed.common.nats.NatsManager;
import net.unnamed.common.packet.PacketRegistry;

import java.util.logging.Logger;

@Plugin(
        id = "velocity-connector",
        name = "Velocity Connector",
        version = "1.0.0"
)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class VelocityConnectorPlugin implements VelocityConnectorApi {
    ProxyServer server;
    Logger logger;
    PluginContainer pluginContainer;
    PacketRegistry packetRegistry;

    @Inject
    public VelocityConnectorPlugin(
            ProxyServer server,
            Logger logger,
            PluginContainer pluginContainer
    ) {
        this.server = server;
        this.logger = logger;
        this.pluginContainer = pluginContainer;
        this.packetRegistry = new PacketRegistry();

        NatsManager.INSTANCE.init();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

    }


    // NatsManager.INSTANCE.init();
}
