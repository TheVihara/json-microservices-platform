package net.unnamed.service.gui.module.listener;

import net.unnamed.common.packet.PacketRegistry;
import net.unnamed.service.gui.api.packet.OpenGuiPacket;
import net.unnamed.service.gui.module.manager.PaperGuiManager;

import java.util.UUID;

public class ServiceGuiListener {
    private final PacketRegistry packetRegistry;
    private final PaperGuiManager guiManager;

    public ServiceGuiListener(PacketRegistry packetRegistry, PaperGuiManager guiManager) {
        this.packetRegistry = packetRegistry;
        this.guiManager = guiManager;
        this.registerListeners();
    }

    private void registerListeners() {
        packetRegistry.registerListener(OpenGuiPacket.class, this::onOpenGui);
    }

    private void onOpenGui(OpenGuiPacket packet) {
        UUID uuid = packet.getPlayerUuid();
        guiManager.openGui(uuid, packet.getGuiData());
    }
}
