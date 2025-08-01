package net.unnamed.service.gui;

import net.unnamed.service.gui.api.Gui;
import net.unnamed.service.gui.session.GuiInventoryViewer;
import net.unnamed.service.gui.session.GuiSession;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GuiManager {
    private final Map<UUID, GuiSession> guiSessions = new ConcurrentHashMap<>();


    /**
     * Opens a GUI for a player
     */
    public boolean openGui(UUID playerUuid, String playerName, String jsonData, String serverContext) {
        // Close any existing GUI for this player first
        closeGui(playerUuid);

        // Create session
        GuiSession session = createSession(playerUuid, playerName, jsonData, serverContext);

        // Draw the GUI (prepare its state)
        gui.draw();

        // Serialize GUI and forward to Minecraft server
        String guiData = guiSerializer.serializeGui(gui);
        minecraftForwarder.forwardOpenGui(
                playerUuid,
                playerName,
                gui.getClass().getSimpleName(),
                guiData,
                serverContext
        );

        getLogger().debug("Opened GUI for player " + playerName + " on server " + serverContext);
        return true;
    }

    /**
     * Closes a player's open GUI
     */
    public void closeGui(UUID playerUuid) {
        GuiSession session = sessionManager.removeSession(playerUuid);
        if (session != null) {
            // Remove viewer from GUI
            session.getGui().removeViewer(session.getViewer());

            // Forward close to Minecraft server
            minecraftForwarder.forwardCloseGui(playerUuid, session.getServerContext());

            getLogger().debug("Closed GUI for player " + session.getPlayerName());
        }
    }

    public GuiSession createSession(UUID playerUuid, String playerName, Gui gui, String serverContext) {
        GuiInventoryViewer viewer = new GuiInventoryViewer(playerUuid, playerName);
        gui.addViewer(viewer);

        GuiSession session = new GuiSession(
                playerUuid,
                playerName,
                gui,
                serverContext,
                System.currentTimeMillis(),
                viewer
        );

        guiSessions.put(playerUuid, session);
        return session;
    }

    public GuiSession getSession(UUID playerUuid) {
        return guiSessions.get(playerUuid);
    }

    public GuiSession removeSession(UUID playerUuid) {
        return guiSessions.remove(playerUuid);
    }

    public boolean hasSession(UUID playerUuid) {
        return guiSessions.containsKey(playerUuid);
    }

    public Map<UUID, GuiSession> getSessions() {
        return new ConcurrentHashMap<>(guiSessions);
    }

    public void clear() {
        guiSessions.clear();
    }
}
