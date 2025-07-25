package net.unnamed.service.gui.api.handler;

import net.unnamed.service.gui.api.Coords;
import net.unnamed.service.gui.api.InventoryViewer;
import net.unnamed.service.gui.api.inventory.ChestServiceInventory;
import net.unnamed.service.gui.api.inventory.ServiceInventory;
import net.unnamed.service.gui.api.layer.InventoryLayer;
import net.unnamed.service.gui.api.slot.Slot;

import java.util.Map;

public interface SlotHandler {
    Map<String, InventoryLayer> getLayers();
    InventoryLayer getLayer(String key);
    Slot getSlot(int x, int y);
    Slot getSlot(Coords coords);
    Slot getSlot(int index);
    void addLayer(String key, InventoryLayer layer);
    void removeLayer(String key);
    void draw(InventoryViewer inventoryViewer);
    //Pair<Integer, ServiceInventory> findSlot(int x, int y);

    static <T extends ServiceInventory> SlotHandler of(T inventory) {
        switch (inventory.getType()) {
            case CHEST -> {
                return new ChestServiceInventory.SlotHandlerImpl((ChestServiceInventory) inventory);
            }
        }

        return null;
    }
}
