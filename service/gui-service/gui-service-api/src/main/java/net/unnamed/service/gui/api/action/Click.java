package net.unnamed.service.gui.api.action;

import net.unnamed.service.gui.api.InventoryViewer;
import net.unnamed.service.gui.api.inventory.ServiceInventory;
import net.unnamed.service.gui.api.slot.Slot;

public interface Click {
    InventoryViewer getViewer();
    ClickType getType();
    ServiceInventory getInventory();
    Slot getClickedSlot();
    boolean isCancelled();
    void setCancelled(boolean cancelled);
}
