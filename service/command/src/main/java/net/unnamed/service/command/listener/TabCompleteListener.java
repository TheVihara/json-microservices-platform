package net.unnamed.service.command.listener;

import net.unnamed.common.nats.NatsManager;
import net.unnamed.common.packet.PacketListener;
import net.unnamed.service.command.CommandRegistry;
import net.unnamed.service.command.api.CommandInfo;
import net.unnamed.service.command.api.packet.TabCompletePacket;
import net.unnamed.service.command.api.packet.TabCompleteResponsePacket;

import java.time.Duration;

public class TabCompleteListener implements PacketListener<TabCompletePacket> {
    private final CommandRegistry commandRegistry;

    public TabCompleteListener(CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void onPacket(TabCompletePacket packet) {
        CommandInfo commandInfo = commandRegistry.getCommandInfo(packet.getCommand());
        if (commandInfo == null) {
            NatsManager.INSTANCE.respond(packet.getOriginalMessage(), new TabCompleteResponsePacket(null));
            return;
        }

        if (!commandInfo.isEnabled()) {
            NatsManager.INSTANCE.respond(packet.getOriginalMessage(), new TabCompleteResponsePacket(null));
            return;
        }

        NatsManager.INSTANCE.request(
                commandInfo.getService() + ".commands",
                packet,
                TabCompleteResponsePacket.class,
                Duration.ofSeconds(5)
        ).whenComplete((response, throwable) -> {
            if (throwable != null) {
                // Handle timeout or error
                throwable.printStackTrace();
                return;
            }

            // Send back the response
            NatsManager.INSTANCE.respond(packet.getOriginalMessage(), response);
        });
    }
}