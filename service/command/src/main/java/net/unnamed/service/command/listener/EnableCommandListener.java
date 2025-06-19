/*
package net.unnamed.service.command.listener;

import net.unnamed.command.api.proto.Command;
import net.unnamed.common.packet.PacketListener;
import net.unnamed.service.command.CommandRegistry;

public class EnableCommandListener implements PacketListener<Command.EnableCommand> {
    private final CommandRegistry commandRegistry;

    public EnableCommandListener(CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void handle(Command.EnableCommand message) {
        commandRegistry.enableCommand(message.getName());
    }
}
*/
