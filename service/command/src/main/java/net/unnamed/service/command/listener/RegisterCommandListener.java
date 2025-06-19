package net.unnamed.service.command.listener;

import com.alibaba.fastjson2.JSON;
import net.unnamed.common.packet.PacketListener;
import net.unnamed.service.command.CommandRegistry;
import net.unnamed.service.command.api.packet.RegisterCommandPacket;

public class RegisterCommandListener implements PacketListener<RegisterCommandPacket> {
    private final CommandRegistry commandRegistry;

    public RegisterCommandListener(CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void onPacket(RegisterCommandPacket packet) {
        commandRegistry.registerCommand(packet.getCommandInfo());
        System.out.println("Registered command: " + JSON.toJSONString(packet.getCommandInfo()));
    }
}
