package net.unnamed.service.gui.api.inventory;

import net.unnamed.service.gui.api.Gui;

public interface Inventory extends Gui {
    InventoryType getType();
    int getRows();
    int getColumns();
}
