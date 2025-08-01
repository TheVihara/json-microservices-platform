package net.unnamed.service.gui.session;

import net.unnamed.service.gui.api.InventoryViewer;
import net.unnamed.service.player.api.PlayerBase;

import java.util.UUID;

/**
 * Implementation of InventoryViewer for tracking GUI viewers
 */
public class GuiInventoryViewer implements InventoryViewer {
    private final UUID playerUuid;
    private final String playerName;
    private final long openedAt;

    public GuiInventoryViewer(UUID playerUuid, String playerName) {
        this.playerUuid = playerUuid;
        this.playerName = playerName;
        this.openedAt = System.currentTimeMillis();
    }

    @Override
    public PlayerBase getPlayer() {
        // In a real implementation, you'd use PlayerService to resolve this
        return null; // Placeholder
    }

    @Override
    public long getOpenedAtMillis() {
        return openedAt;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getPlayerName() {
        return playerName;
    }
}
