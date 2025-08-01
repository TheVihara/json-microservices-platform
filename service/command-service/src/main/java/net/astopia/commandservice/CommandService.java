package net.astopia.commandservice;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.astopia.commandservice.api.packet.CommandExecutionPacket;
import net.astopia.commandservice.api.packet.CommandSuggestionRequestPacket;
import net.astopia.commandservice.api.packet.CommandSuggestionResponsePacket;
import net.astopia.commandservice.api.packet.RegisterCommandPacket;
import net.astopia.commandservice.listener.CommandExecutionListener;
import net.astopia.commandservice.listener.CommandSuggestionListener;
import net.unnamed.common.config.CustomYamlPersistenceDelegateFactory;
import net.unnamed.service.common.PlatformService;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommandService extends PlatformService {
    CustomYamlPersistenceDelegateFactory yamlPersistenceDelegateFactory = new CustomYamlPersistenceDelegateFactory();

    @Override
    public void onLoad() {
        packetRegistry.subscribe("command_service.packets");

        packetRegistry.registerPacket(CommandExecutionPacket.ID, CommandExecutionPacket.class);
        packetRegistry.registerPacket(CommandSuggestionRequestPacket.ID, CommandSuggestionRequestPacket.class);
        packetRegistry.registerPacket(CommandSuggestionResponsePacket.ID, CommandSuggestionResponsePacket.class);
        packetRegistry.registerPacket(RegisterCommandPacket.ID, RegisterCommandPacket.class);

        packetRegistry.registerListener(CommandExecutionPacket.class, new CommandExecutionListener());
        packetRegistry.registerListener(CommandSuggestionRequestPacket.class, new CommandSuggestionListener());
    }

    @Override
    public void onStop() {

    }
}
