package net.unnamed.service.command.listener;

import net.unnamed.command.api.proto.Command;
import net.unnamed.common.packet.PacketListener;

public class ExecuteCommandListener implements PacketListener<Command.ExecuteCommand> {
    @Override
    public void handle(Command.ExecuteCommand message) {

    }
}
