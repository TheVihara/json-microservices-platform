package net.astopia.commandservice.api.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.astopia.commandservice.api.manager.ServiceCommandManager;
import net.astopia.commandservice.api.packet.CommandExecutionPacket;
import net.unnamed.common.packet.PacketListener;

import java.util.function.Function;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CommandExecutionListener<C> implements PacketListener<CommandExecutionPacket> {
    ServiceCommandManager<C> commandManager;
    Function<CommandExecutionPacket, C> mapper;

    @Override
    public void onPacket(CommandExecutionPacket packet) {
        commandManager.commandExecutor().executeCommand(mapper.apply(packet), packet.getCommandInput());
    }
}
