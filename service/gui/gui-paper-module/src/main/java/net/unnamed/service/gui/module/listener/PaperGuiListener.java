package net.unnamed.service.gui.module.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.unnamed.service.gui.api.action.Click;
import net.unnamed.service.gui.api.action.DefaultClick;
import net.unnamed.service.gui.api.action.ClickType;
import net.unnamed.service.gui.api.action.ShiftClick;
import net.unnamed.service.gui.api.handler.SlotHandler;
import net.unnamed.service.gui.api.slot.Slot;
import net.unnamed.service.gui.module.gui.KnowledgeListGui;
import net.unnamed.service.gui.module.inventory.CustomInventory;
import net.unnamed.service.gui.module.inventory.CustomInventoryHolder;
import net.unnamed.service.gui.module.inventory.PlayerInventoryViewer;
import net.unnamed.service.gui.module.manager.KnowledgeManager;
import net.unnamed.service.gui.module.manager.PaperGuiManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PaperGuiListener implements Listener {
    PaperGuiManager paperGuiManager;
    KnowledgeManager knowledgeManager;

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();

        if (!(inventory.getHolder() instanceof CustomInventoryHolder(CustomInventory customInventory))) {
            return;
        }

        event.titleOverride(customInventory.getTitle());
        customInventory.open(PlayerInventoryViewer.of((Player) event.getPlayer()));
    }

    @EventHandler
    public void onClose(InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();

        if (!(inventory.getHolder() instanceof CustomInventoryHolder(CustomInventory customInventory))) {
            return;
        }

        customInventory.close(PlayerInventoryViewer.of((Player) event.getPlayer()));
    }

    // TODO: PAGED ZONES, ASYNC PAGE LOADING WITH AN LOADING ITEM, ANIMATION SYSTEM

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();

        if (!(inventory.getHolder() instanceof CustomInventoryHolder(CustomInventory customInventory))) {
            return;
        }

        SlotHandler slotHandler = customInventory.getSlotHandler();
        Slot slot = slotHandler.getSlot(event.getSlot());

        if (slot == null) {
            Bukkit.getLogger().warning("Slot not found for slot " + event.getSlot());
            return;
        }

        ClickType clickType;

        if (event.getClick().isLeftClick()) {
            clickType = ClickType.LEFT;
        } else {
            clickType = ClickType.RIGHT;
        }

        Click click;

        if (event.getClick().isShiftClick()) {
            click = new ShiftClick(PlayerInventoryViewer.of(player), clickType, customInventory, slot);
        } else {
            click = new DefaultClick(PlayerInventoryViewer.of(player), clickType, customInventory, slot);
        }

        slot.onClick(click);
        if (click.isCancelled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        paperGuiManager.openGui(player, new KnowledgeListGui(knowledgeManager));
    }
}
