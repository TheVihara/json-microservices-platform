package net.unnamed.service.gui.minecraft;

import net.unnamed.common.nats.NatsManager;
import net.unnamed.common.packet.Packet;
import net.unnamed.service.gui.GuiService;
import net.unnamed.service.gui.api.Gui;
import net.unnamed.service.gui.api.item.Item;
import net.unnamed.service.gui.api.packet.*;

import java.util.Map;
import java.util.UUID;

/**
 * Forwards GUI actions to Minecraft servers
 */
public class MinecraftForwarder {
    private final NatsManager natsManager;

    public MinecraftForwarder(GuiService guiService) {
        this.guiService = guiService;
    }

    public void forwardOpenGui(UUID playerUuid, String playerName, String guiType, String guiData, String serverContext) {
        OpenGuiPacket packet = new OpenGuiPacket(playerUuid, playerName, guiType, guiData, serverContext);
        publishToMinecraft(packet, serverContext);
    }

    public void forwardCloseGui(UUID playerUuid, String serverContext) {
        CloseGuiPacket packet = new CloseGuiPacket(playerUuid);
        publishToMinecraft(packet, serverContext);
    }

    public void forwardSetItem(UUID playerUuid, int slot, String itemData, String serverContext) {
        SetItemPacket packet = new SetItemPacket(playerUuid, slot, itemData);
        publishToMinecraft(packet, serverContext);
    }

    public void forwardSetItems(UUID playerUuid, Map<Integer, String> itemsData, String serverContext) {
        SetItemsPacket packet = new SetItemsPacket(playerUuid, itemsData);
        publishToMinecraft(packet, serverContext);
    }

    public void forwardClearGui(UUID playerUuid, String serverContext) {
        ClearGuiPacket packet = new ClearGuiPacket(playerUuid);
        publishToMinecraft(packet, serverContext);
    }

    public void forwardRefreshGui(UUID playerUuid, String playerName, String guiType, String guiData, String serverContext) {
        RefreshGuiPacket packet = new RefreshGuiPacket(playerUuid, playerName, guiType, guiData);
        publishToMinecraft(packet, serverContext);
    }

    private void publishToMinecraft(Packet packet, String serverContext) {
        String channel = serverContext != null ? 
            "minecraft." + serverContext + ".gui" : 
            "minecraft.gui";

        guiService.getNatsManager().publish(channel, packet);
        guiService.getLogger().debug("Forwarded packet to " + channel);
    }
}
