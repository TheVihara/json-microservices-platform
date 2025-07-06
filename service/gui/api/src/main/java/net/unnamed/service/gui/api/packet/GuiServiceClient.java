package net.unnamed.service.gui.api.packet;

import com.alibaba.fastjson2.JSON;
import net.unnamed.common.nats.NatsManager;
import net.unnamed.service.gui.api.Gui;
import net.unnamed.service.gui.api.item.Item;
import net.unnamed.service.player.api.PlayerBase;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Client for interacting with the GUI service from other services
 */
public class GuiServiceClient {
    private static final String GUI_SERVICE_CHANNEL = "service.gui.packets";
    private final GuiSerializer serializer = new GuiSerializer();

    /**
     * Opens a GUI for a player
     */
    public CompletableFuture<Boolean> openGui(PlayerBase player, Gui gui, String serverContext) {
        String guiData = serializer.serializeGui(gui);

        OpenGuiPacket packet = new OpenGuiPacket(
            player.getUuid(), 
            player.getName(), 
            gui.getClass().getSimpleName(), 
            guiData, 
            serverContext
        );

        return NatsManager.INSTANCE.request(
            GUI_SERVICE_CHANNEL,
            packet,
            GuiResponsePacket.class,
            Duration.ofSeconds(5)
        ).thenApply(GuiResponsePacket::isSuccess);
    }

    /**
     * Opens a GUI for a player (no server context)
     */
    public CompletableFuture<Boolean> openGui(PlayerBase player, Gui gui) {
        return openGui(player, gui, null);
    }

    /**
     * Sets a single item in the player's currently opened GUI
     */
    public CompletableFuture<Boolean> setItem(PlayerBase player, int slot, Item item) {
        String itemData = serializer.serializeItem(item);
        SetItemPacket packet = new SetItemPacket(player.getUuid(), slot, itemData);

        return NatsManager.INSTANCE.request(
            GUI_SERVICE_CHANNEL,
            packet,
            GuiResponsePacket.class,
            Duration.ofSeconds(5)
        ).thenApply(GuiResponsePacket::isSuccess);
    }

    /**
     * Sets multiple items in the player's currently opened GUI
     */
    public CompletableFuture<Boolean> setItems(PlayerBase player, Map<Integer, Item> items) {
        Map<Integer, String> itemsData = items.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> serializer.serializeItem(entry.getValue())
            ));

        SetItemsPacket packet = new SetItemsPacket(player.getUuid(), itemsData);

        return NatsManager.INSTANCE.request(
            GUI_SERVICE_CHANNEL,
            packet,
            GuiResponsePacket.class,
            Duration.ofSeconds(5)
        ).thenApply(GuiResponsePacket::isSuccess);
    }

    /**
     * Clears all items from the player's currently opened GUI
     */
    public CompletableFuture<Boolean> clearGui(PlayerBase player) {
        ClearGuiPacket packet = new ClearGuiPacket(player.getUuid());

        return NatsManager.INSTANCE.request(
            GUI_SERVICE_CHANNEL,
            packet,
            GuiResponsePacket.class,
            Duration.ofSeconds(5)
        ).thenApply(GuiResponsePacket::isSuccess);
    }

    /**
     * Forces a complete refresh of the player's GUI
     */
    public CompletableFuture<Boolean> refreshGui(PlayerBase player) {
        RefreshGuiPacket packet = new RefreshGuiPacket(player.getUuid());

        return NatsManager.INSTANCE.request(
            GUI_SERVICE_CHANNEL,
            packet,
            GuiResponsePacket.class,
            Duration.ofSeconds(5)
        ).thenApply(GuiResponsePacket::isSuccess);
    }

    /**
     * Closes any open GUI for a player
     */
    public CompletableFuture<Boolean> closeGui(PlayerBase player) {
        CloseGuiPacket packet = new CloseGuiPacket(player.getUuid());

        return NatsManager.INSTANCE.request(
            GUI_SERVICE_CHANNEL,
            packet,
            GuiResponsePacket.class,
            Duration.ofSeconds(5)
        ).thenApply(GuiResponsePacket::isSuccess);
    }
}
