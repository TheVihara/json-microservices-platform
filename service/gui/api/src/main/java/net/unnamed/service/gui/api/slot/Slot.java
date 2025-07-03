package net.unnamed.service.gui.api.slot;

import net.unnamed.service.gui.api.action.Click;
import net.unnamed.service.gui.api.inventory.Inventory;
import net.unnamed.service.gui.api.item.Item;

import java.util.Map;

public interface Slot {
    Map.Entry<Number, Number> getCoords();
    Inventory getInventory();
    Item getItem();
    void setItem(Item item);
    void onClick(Click click);
}
