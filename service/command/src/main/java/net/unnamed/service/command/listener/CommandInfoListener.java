package net.unnamed.service.command.listener;

import net.unnamed.command.api.proto.Command;
import net.unnamed.common.packet.PacketListener;
import net.unnamed.service.command.CommandRegistry;

public class CommandInfoListener implements PacketListener<Command.CommandInfo> {
    private final CommandRegistry commandRegistry;

    public CommandInfoListener(CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void handle(Command.CommandInfo message) {
        commandRegistry.registerCommand(message);
    }
}
