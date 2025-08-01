package net.astopia.commandservice.listener;

import net.astopia.commandservice.api.packet.CommandSuggestionRequestPacket;
import net.astopia.commandservice.api.packet.CommandSuggestionResponsePacket;
import net.unnamed.common.nats.NatsManager;
import net.unnamed.common.packet.PacketListener;

import java.time.Duration;

public class CommandSuggestionListener implements PacketListener<CommandSuggestionRequestPacket> {
    @Override
    public void onPacket(CommandSuggestionRequestPacket packet) {
        String service = packet.getServiceContext();
        NatsManager.INSTANCE.request(
                service + "_service.commands",
                packet,
                CommandSuggestionResponsePacket.class,
                Duration.ofSeconds(3)
        ).thenAccept(response -> {
            NatsManager.INSTANCE.respond(packet.getOriginalMessage(), response);
        }).join();
    }
}
