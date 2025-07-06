package net.unnamed.service.command.module.listener;

import net.unnamed.common.packet.PacketListener;
import net.unnamed.service.command.api.packet.RegisterCommandPacket;
import net.unnamed.service.command.module.CommandModule;

public class CommandRegistrationListener implements PacketListener<RegisterCommandPacket> {
    private final CommandModule module;

    public CommandRegistrationListener(CommandModule module) {
        this.module = module;
    }

    @Override
    public void onPacket(RegisterCommandPacket packet) {
        if (packet.getCommandInfo() != null) {
/*
            module.registerCommand(packet.getCommandInfo());
*/
        }
    }
}
