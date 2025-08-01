package net.astopia.velocitycommandsystem;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.astopia.commandservice.api.packet.CommandExecutionPacket;
import net.astopia.commandservice.api.packet.CommandSuggestionRequestPacket;
import net.astopia.commandservice.api.packet.CommandSuggestionResponsePacket;
import net.astopia.commandservice.api.packet.RegisterCommandPacket;
import net.astopia.velocitycommandsystem.coordinator.CustomExecutionCoordinator;
import net.astopia.velocitycommandsystem.listener.RegisterCommandListener;
import net.astopia.velocityconnector.api.VelocityConnectorApi;
import net.unnamed.common.packet.PacketRegistry;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.velocity.VelocityCommandManager;

import java.util.logging.Logger;

@Plugin(
        id = "command-system",
        name = "Velocity Command System",
        version = "1.0.0",
        dependencies = {
                @Dependency(id = "velocity-connector")
        }
)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class CommandSystemPlugin {
    ProxyServer server;
    Logger logger;
    PluginContainer pluginContainer;
    @NonFinal PacketRegistry packetRegistry;

    ExecutionCoordinator<CommandSource> executionCoordinator = ExecutionCoordinator.simpleCoordinator();
    @NonFinal VelocityCommandManager<CommandSource> velocityCommandManager;

    @Inject
    public CommandSystemPlugin(ProxyServer server, Logger logger, PluginContainer pluginContainer) {
        this.server = server;
        this.logger = logger;
        this.pluginContainer = pluginContainer;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.velocityCommandManager = new VelocityCommandManager<>(
                pluginContainer,
                server,
                new CustomExecutionCoordinator(),
                SenderMapper.identity()
        );

        VelocityConnectorApi velocityConnectorApi = VelocityConnectorApi.getInstance(server);
        this.packetRegistry = velocityConnectorApi.getPacketRegistry();

        packetRegistry.subscribe("command_service.packets");

        packetRegistry.registerPacket(CommandExecutionPacket.ID, CommandExecutionPacket.class);
        packetRegistry.registerPacket(CommandSuggestionRequestPacket.ID, CommandSuggestionRequestPacket.class);
        packetRegistry.registerPacket(CommandSuggestionResponsePacket.ID, CommandSuggestionResponsePacket.class);
        packetRegistry.registerPacket(RegisterCommandPacket.ID, RegisterCommandPacket.class);

        packetRegistry.registerListener(RegisterCommandPacket.class, new RegisterCommandListener(velocityCommandManager));
    }
}
