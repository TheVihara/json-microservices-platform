package net.unnamed.service.gui;

import com.alibaba.fastjson2.JSON;
import net.unnamed.common.packet.Packet;
import net.unnamed.service.common.PlatformService;
import net.unnamed.service.gui.api.Gui;
import net.unnamed.service.gui.api.item.Item;
import net.unnamed.service.gui.api.packet.*;
import net.unnamed.service.gui.handler.GuiPacketHandler;
import net.unnamed.service.gui.minecraft.MinecraftForwarder;
import net.unnamed.service.gui.session.GuiSession;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing GUIs across the platform
 */
public class GuiService extends PlatformService {
    private final GuiManager guiManager;
    private final MinecraftForwarder minecraftForwarder;
    private final GuiSerializer guiSerializer;

    public GuiService() {
        this.guiManager = new GuiManager();
        this.minecraftForwarder = new MinecraftForwarder(this);
        this.guiSerializer = new GuiSerializer();
    }

    @Override
    public void onLoad() {
        registerPackets();

        packetHandler.registerListeners();
        packetRegistry.subscribe("service.gui.packets");
    }

    @Override
    public void onStop() {
        guiManager.getSessions().values().forEach(session -> {
            minecraftForwarder.forwardCloseGui(session.getPlayerUuid(), session.getServerContext());
        });

        guiManager.clear();
        getLogger().info("GUI Service stopped");
    }

    private void registerPackets() {
        getPacketRegistry().registerPacket(OpenGuiPacket.ID, OpenGuiPacket.class);
        getPacketRegistry().registerPacket(CloseGuiPacket.ID, CloseGuiPacket.class);
        getPacketRegistry().registerPacket(ClickGuiPacket.ID, ClickGuiPacket.class);
        getPacketRegistry().registerPacket(SetItemPacket.ID, SetItemPacket.class);
        getPacketRegistry().registerPacket(SetItemsPacket.ID, SetItemsPacket.class);
        getPacketRegistry().registerPacket(ClearGuiPacket.ID, ClearGuiPacket.class);
        getPacketRegistry().registerPacket(RefreshGuiPacket.ID, RefreshGuiPacket.class);
        getPacketRegistry().registerPacket(GuiResponsePacket.ID, GuiResponsePacket.class);
    }

    /**
     * Sets a single item in a player's open GUI
     */
    public boolean setItem(UUID playerUuid, int slot, Item item) {
        GuiSession session = sessionManager.getSession(playerUuid);
        if (session == null) {
            getLogger().warn("Attempted to set item for player " + playerUuid + " but no GUI session found");
            return false;
        }

        try {
            // Update GUI state
            updateGuiSlot(session.getGui(), slot, item);

            // Serialize item and forward to Minecraft server
            String itemData = guiSerializer.serializeItem(item);
            minecraftForwarder.forwardSetItem(playerUuid, slot, itemData, session.getServerContext());

            getLogger().debug("Set item at slot " + slot + " for player " + session.getPlayerName());
            return true;
        } catch (Exception e) {
            getLogger().error("Error setting item for player " + playerUuid, e);
            return false;
        }
    }

    /**
     * Sets multiple items in a player's open GUI
     */
    public boolean setItems(UUID playerUuid, Map<Integer, Item> items) {
        GuiSession session = sessionManager.getSession(playerUuid);
        if (session == null) {
            getLogger().warn("Attempted to set items for player " + playerUuid + " but no GUI session found");
            return false;
        }

        try {
            // Update GUI state
            for (Map.Entry<Integer, Item> entry : items.entrySet()) {
                updateGuiSlot(session.getGui(), entry.getKey(), entry.getValue());
            }

            // Serialize items and forward to Minecraft server
            Map<Integer, String> itemsData = new ConcurrentHashMap<>();
            for (Map.Entry<Integer, Item> entry : items.entrySet()) {
                itemsData.put(entry.getKey(), guiSerializer.serializeItem(entry.getValue()));
            }
            minecraftForwarder.forwardSetItems(playerUuid, itemsData, session.getServerContext());

            getLogger().debug("Set " + items.size() + " items for player " + session.getPlayerName());
            return true;
        } catch (Exception e) {
            getLogger().error("Error setting items for player " + playerUuid, e);
            return false;
        }
    }

    /**
     * Clears all items from a player's open GUI
     */
    public boolean clearGui(UUID playerUuid) {
        GuiSession session = sessionManager.getSession(playerUuid);
        if (session == null) {
            return false;
        }

        try {
            // Clear all slots in the GUI
            clearGuiSlots(session.getGui());

            // Forward clear to Minecraft server
            minecraftForwarder.forwardClearGui(playerUuid, session.getServerContext());

            getLogger().debug("Cleared GUI for player " + session.getPlayerName());
            return true;
        } catch (Exception e) {
            getLogger().error("Error clearing GUI for player " + playerUuid, e);
            return false;
        }
    }

    /**
     * Refreshes a player's open GUI (complete redraw)
     */
    public boolean refreshGui(UUID playerUuid) {
        GuiSession session = sessionManager.getSession(playerUuid);
        if (session == null) {
            return false;
        }

        try {
            // Redraw the entire GUI
            session.getGui().draw();

            // Serialize and forward refresh to Minecraft server
            String guiData = guiSerializer.serializeGui(session.getGui());
            minecraftForwarder.forwardRefreshGui(
                playerUuid,
                session.getPlayerName(),
                session.getGui().getClass().getSimpleName(),
                guiData,
                session.getServerContext()
            );

            getLogger().debug("Refreshed GUI for player " + session.getPlayerName());
            return true;
        } catch (Exception e) {
            getLogger().error("Error refreshing GUI for player " + playerUuid, e);
            return false;
        }
    }

    /**
     * Handles a click on a player's open GUI
     */
    public void handleClick(UUID playerUuid, int slot, String clickType, Map<String, Object> clickData) {
        GuiSession session = sessionManager.getSession(playerUuid);
        if (session == null) {
            getLogger().warn("Received click for player " + playerUuid + " but no GUI session found");
            return;
        }

        try {
            // Handle click through the GUI's slot handler
            handleGuiClick(session.getGui(), slot, clickType, clickData);

            getLogger().debug("Handled click for player " + session.getPlayerName() + " on slot " + slot);
        } catch (Exception e) {
            getLogger().error("Error handling click for player " + playerUuid, e);
        }
    }

    /**
     * Sends a response packet
     */
    public void respondToPacket(Packet packet, GuiResponsePacket response) {
        if (packet.getOriginalMessage() != null && packet.getOriginalMessage().getReplyTo() != null) {
            getNatsManager().getConnection().publish(
                packet.getOriginalMessage().getReplyTo(),
                JSON.toJSONBytes(response)
            );
        }
    }

    // Helper methods
    private void updateGuiSlot(Gui gui, int slot, Item item) {
        // Implementation depends on your GUI structure
        // This would use the SlotHandler from the GUI
        gui.getSlotHandler().setItem(slot, item);
    }

    private void clearGuiSlots(Gui gui) {
        // Implementation to clear all slots in the GUI
        // This would iterate through slots and clear them
    }

    private void handleGuiClick(Gui gui, int slot, String clickType, Map<String, Object> clickData) {
        // Implementation for handling clicks
        // This would delegate to the GUI's SlotHandler
    }

    // Getters
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public GuiSerializer getGuiSerializer() {
        return guiSerializer;
    }
}
