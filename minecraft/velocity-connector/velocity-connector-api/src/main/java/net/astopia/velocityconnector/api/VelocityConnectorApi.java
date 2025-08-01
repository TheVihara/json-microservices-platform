package net.astopia.velocityconnector.api;

import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import net.unnamed.common.packet.PacketRegistry;

public interface VelocityConnectorApi {
    PacketRegistry getPacketRegistry();

    static VelocityConnectorApi getInstance(ProxyServer server) {
        PluginContainer pluginContainer = server.getPluginManager().getPlugin("velocity-connector")
                .orElse(null);

        if (pluginContainer == null) {
            return null;
        }

        return (VelocityConnectorApi) pluginContainer.getInstance().orElse(null);
    }
}
