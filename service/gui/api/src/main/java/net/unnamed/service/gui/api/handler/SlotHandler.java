package net.unnamed.service.gui.api.handler;

import net.unnamed.service.gui.api.inventory.Inventory;
import net.unnamed.service.gui.api.slot.Slot;
import org.antlr.v4.runtime.misc.Pair;

import java.util.List;
import java.util.Map;

public interface SlotHandler {
    List<Map.Entry<Integer, Integer>> getUpdatingSlots();
    Map<Map.Entry<Integer, Integer>, Slot> getSlotCoords();
    void setSlot(Map.Entry<Integer, Integer> coords, Slot slot);
    Pair<Integer, Inventory> findSlot(int x, int y);
}
