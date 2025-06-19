package net.unnamed.service.command.api.listener;

import net.unnamed.common.nats.NatsManager;
import net.unnamed.common.packet.PacketListener;
import net.unnamed.service.command.api.AnnotationCommandRegistry;
import net.unnamed.service.command.api.packet.TabCompletePacket;
import net.unnamed.service.command.api.packet.TabCompleteResponsePacket;

import java.util.List;

public class TabCompleteListener implements PacketListener<TabCompletePacket> {
    private final AnnotationCommandRegistry annotationRegistry;

    public TabCompleteListener(AnnotationCommandRegistry annotationRegistry) {
        this.annotationRegistry = annotationRegistry;
    }

    @Override
    public void onPacket(TabCompletePacket packet) {
        List<String> completions = annotationRegistry.tabComplete(
                packet.getSender(),
                packet.getCommand(),
                packet.getArgs()
        );

        NatsManager.INSTANCE.respond(packet.getOriginalMessage(),
                new TabCompleteResponsePacket(completions));
    }
}
