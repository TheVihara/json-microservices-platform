package net.unnamed.service.gui.api.slot;

import net.unnamed.service.gui.api.Coords;
import net.unnamed.service.gui.api.action.Click;
import net.unnamed.service.gui.api.action.DefaultClick;
import net.unnamed.service.gui.api.impl.SlotImpl;
import net.unnamed.service.gui.api.inventory.ServiceInventory;
import net.unnamed.service.gui.api.item.Item;
import net.unnamed.service.gui.api.layer.InventoryLayer;

import java.util.Map;
import java.util.function.Consumer;

public interface Slot {
    Coords getCoords();
    InventoryLayer getLayer();
    Item getItem();
    void setItem(Item item);
    void onClick(Click click);
    void setClickConsumer(Consumer<Click> clickConsumer);
    void setLayer(InventoryLayer layer);
    boolean isVisible();
    void setVisible(boolean visible);

    static Slot of(int x, int y, InventoryLayer layer, ServiceInventory inventory, Item item) {
        return new SlotImpl(x, y, layer, inventory, item);
    }

    static Slot of(int x, int y, InventoryLayer layer, ServiceInventory inventory, Item item, Consumer<Click> click) {
        return new SlotImpl(x, y, layer, inventory, item, click);
    }

    static Slot of(Coords coords, InventoryLayer layer, ServiceInventory inventory, Item item) {
        return new SlotImpl(coords, layer, inventory, item);
    }

    static Slot of(Coords coords, InventoryLayer layer, ServiceInventory inventory, Item item, Consumer<Click> click) {
        return new SlotImpl(coords.getX(), coords.getY(), layer, inventory, item, click);
    }
}
