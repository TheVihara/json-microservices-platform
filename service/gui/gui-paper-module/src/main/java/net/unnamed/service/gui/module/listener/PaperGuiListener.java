package net.unnamed.service.gui.module.listener;

import net.unnamed.common.packet.PacketRegistry;
import net.unnamed.service.gui.api.packet.OpenGuiPacket;
import net.unnamed.service.gui.module.PaperGuiManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PaperGuiListener {
    private final PacketRegistry packetRegistry;
    private final PaperGuiManager guiManager;

    public PaperGuiListener(PacketRegistry packetRegistry, PaperGuiManager guiManager) {
        this.packetRegistry = packetRegistry;
        this.guiManager = guiManager;
        this.registerListeners();
    }

    private void registerListeners() {
        packetRegistry.registerListener(OpenGuiPacket.class, this::onOpenGui);
    }

    private void onOpenGui(OpenGuiPacket packet) {
        UUID uuid = packet.getPlayerUuid();
        guiManager.openGui(uuid);
    }
}
