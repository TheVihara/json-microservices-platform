/*
package net.unnamed.service.command.listener;

import net.unnamed.command.api.proto.Command;
import net.unnamed.common.packet.PacketListener;
import net.unnamed.service.command.CommandRegistry;

public class DisableCommandListener implements PacketListener<Command.DisableCommand> {
    private final CommandRegistry commandRegistry;

    public DisableCommandListener(CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void handle(Command.DisableCommand message) {
        commandRegistry.disableCommand(message.getName());
    }
}
*/
