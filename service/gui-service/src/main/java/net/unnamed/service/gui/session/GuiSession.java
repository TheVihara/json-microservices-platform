package net.unnamed.service.gui.session;

import net.unnamed.service.gui.api.Gui;
import net.unnamed.service.gui.api.InventoryViewer;

import java.util.UUID;

/**
 * Represents an active GUI session for a player
 */
public class GuiSession {
    private final UUID playerUuid;
    private final String playerName;
    private final Gui gui;
    private final String serverContext;
    private final long openedAt;
    private final InventoryViewer viewer;

    public GuiSession(UUID playerUuid, String playerName, Gui gui, String serverContext, long openedAt, InventoryViewer viewer) {
        this.playerUuid = playerUuid;
        this.playerName = playerName;
        this.gui = gui;
        this.serverContext = serverContext;
        this.openedAt = openedAt;
        this.viewer = viewer;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Gui getGui() {
        return gui;
    }

    public String getServerContext() {
        return serverContext;
    }

    public long getOpenedAt() {
        return openedAt;
    }

    public InventoryViewer getViewer() {
        return viewer;
    }
}
