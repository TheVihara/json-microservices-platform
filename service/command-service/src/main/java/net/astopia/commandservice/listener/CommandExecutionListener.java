package net.astopia.commandservice.listener;

import net.astopia.commandservice.api.packet.CommandExecutionPacket;
import net.unnamed.common.nats.NatsManager;
import net.unnamed.common.packet.PacketListener;

public class CommandExecutionListener implements PacketListener<CommandExecutionPacket> {
    @Override
    public void onPacket(CommandExecutionPacket packet) {
        String service = packet.getServiceContext();
        NatsManager.INSTANCE.publish(service + "_service.commands", packet);
    }
}
