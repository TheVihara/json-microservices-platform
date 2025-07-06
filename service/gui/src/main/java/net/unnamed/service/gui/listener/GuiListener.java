package net.unnamed.service.gui.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import net.unnamed.common.packet.PacketRegistry;
import net.unnamed.service.gui.GuiManager;
import net.unnamed.service.gui.api.Gui;
import net.unnamed.service.gui.api.item.Item;
import net.unnamed.service.gui.api.packet.*;
import net.unnamed.service.gui.api.serializer.SerializerType;

import java.util.Map;

public class GuiListener {
    private final GuiManager guiManager;
    private final PacketRegistry packetRegistry;

    public GuiListener(GuiManager guiManager,
                       PacketRegistry packetRegistry) {
        this.guiManager = guiManager;
        this.packetRegistry = packetRegistry;
        this.registerListeners();
    }

    public void registerListeners() {
        packetRegistry.registerListener(OpenGuiPacket.class, this::handleOpenGui);
        packetRegistry.registerListener(CloseGuiPacket.class, this::handleCloseGui);
        packetRegistry.registerListener(ClickGuiPacket.class, this::handleClickGui);
        packetRegistry.registerListener(SetItemPacket.class, this::handleSetItem);
        packetRegistry.registerListener(SetItemsPacket.class, this::handleSetItems);
        packetRegistry.registerListener(ClearGuiPacket.class, this::handleClearGui);
        packetRegistry.registerListener(RefreshGuiPacket.class, this::handleRefreshGui);
    }

    private void handleOpenGui(OpenGuiPacket packet) {
        try {
            JSONObject jsonObject = JSON.parseObject(packet.getGuiData());
            Gui gui = SerializerType.deserialize(jsonObject);

            boolean success = guiManager.openGui(
                    packet.getPlayerUuid(),
                    packet.getPlayerName(),
                    gui,
                    packet.getServerContext()
            );

            GuiResponsePacket response = new GuiResponsePacket(
                    success,
                    success ? "GUI opened successfully" : "Failed to open GUI"
            );

            guiService.respondToPacket(packet, response);
        } catch (Exception e) {
            guiService.getLogger().error("Error handling OpenGuiPacket", e);
            guiService.respondToPacket(packet, new GuiResponsePacket(false, "Error: " + e.getMessage()));
        }
    }

    private void handleCloseGui(CloseGuiPacket packet) {
        guiService.closeGui(packet.getPlayerUuid());
        guiService.respondToPacket(packet, new GuiResponsePacket(true, "GUI closed successfully"));
    }

    private void handleClickGui(ClickGuiPacket packet) {
        guiService.handleClick(packet.getPlayerUuid(), packet.getSlot(), packet.getClickType(), packet.getClickData());
        guiService.respondToPacket(packet, new GuiResponsePacket(true, "Click handled"));
    }

    private void handleSetItem(SetItemPacket packet) {
        try {
            Item item = guiService.getGuiSerializer().deserializeItem(packet.getItemData());
            boolean success = guiService.setItem(packet.getPlayerUuid(), packet.getSlot(), item);

            guiService.respondToPacket(packet, new GuiResponsePacket(success, success ? "Item set" : "Failed to set item"));
        } catch (Exception e) {
            guiService.getLogger().error("Error handling SetItemPacket", e);
            guiService.respondToPacket(packet, new GuiResponsePacket(false, "Error: " + e.getMessage()));
        }
    }

    private void handleSetItems(SetItemsPacket packet) {
        try {
            Map<Integer, Item> items = guiService.getGuiSerializer().deserializeItems(packet.getItemsData());
            boolean success = guiService.setItems(packet.getPlayerUuid(), items);

            guiService.respondToPacket(packet, new GuiResponsePacket(success, success ? "Items set" : "Failed to set items"));
        } catch (Exception e) {
            guiService.getLogger().error("Error handling SetItemsPacket", e);
            guiService.respondToPacket(packet, new GuiResponsePacket(false, "Error: " + e.getMessage()));
        }
    }

    private void handleClearGui(ClearGuiPacket packet) {
        boolean success = guiService.clearGui(packet.getPlayerUuid());
        guiService.respondToPacket(packet, new GuiResponsePacket(success, success ? "GUI cleared" : "Failed to clear GUI"));
    }

    private void handleRefreshGui(RefreshGuiPacket packet) {
        boolean success = guiService.refreshGui(packet.getPlayerUuid());
        guiService.respondToPacket(packet, new GuiResponsePacket(success, success ? "GUI refreshed" : "Failed to refresh GUI"));
    }
}
