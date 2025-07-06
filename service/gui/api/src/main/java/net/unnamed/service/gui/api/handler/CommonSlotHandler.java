package net.unnamed.service.gui.api.handler;

import net.unnamed.service.gui.api.inventory.ServiceInventory;
import net.unnamed.service.gui.api.slot.Slot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CommonSlotHandler<T extends ServiceInventory> implements SlotHandler {
    protected final T openedInventory;
    protected final List<Map.Entry<Integer, Integer>> updatingSlots = new ArrayList<>();
    protected final Map<Map.Entry<Integer, Integer>, Slot> slotCoords = new HashMap<>();

    public CommonSlotHandler(T openedInventory) {
        this.openedInventory = openedInventory;
    }

    @Override
    public List<Map.Entry<Integer, Integer>> getUpdatingSlots() {
        return updatingSlots;
    }

    @Override
    public Map<Map.Entry<Integer, Integer>, Slot> getSlotCoords() {
        return slotCoords;
    }

    @Override
    public void setSlot(Map.Entry<Integer, Integer> coords, Slot slot) {
        slotCoords.put(coords, slot);
    }

    public ServiceInventory getOpenedInventory() {
        return openedInventory;
    }
}
