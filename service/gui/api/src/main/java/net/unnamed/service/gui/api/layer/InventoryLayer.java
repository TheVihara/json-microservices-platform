package net.unnamed.service.gui.api.layer;

import net.unnamed.service.gui.api.Coords;
import net.unnamed.service.gui.api.InventoryViewer;
import net.unnamed.service.gui.api.inventory.ServiceInventory;
import net.unnamed.service.gui.api.slot.Slot;

import java.util.Map;

public interface InventoryLayer {
    int getWeight();
    Coords getFirstCoords();
    Coords getSecondCoords();
    Map<Coords, Slot> getSlots();
    Slot getSlot(Coords coords);
    Slot getSlot(int x, int y, ServiceInventory inventory);
    Slot getSlot(int index, ServiceInventory inventory);
    void setSlot(int x, int y, Slot slot);
    void setSlot(Coords coords, Slot slot);
    int findSlot(int x, int y, ServiceInventory inventory);
    int findSlot(Coords coords, ServiceInventory inventory);
    boolean isVisible();
    boolean containsCoords(Coords coords);
    void setVisible(boolean visible);
    void draw(InventoryViewer viewer, ServiceInventory inventory);
}
