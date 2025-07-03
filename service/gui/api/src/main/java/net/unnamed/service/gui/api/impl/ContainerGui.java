package net.unnamed.service.gui.api.impl;

import net.unnamed.service.gui.api.Gui;
import net.unnamed.service.gui.api.InventoryViewer;
import net.unnamed.service.gui.api.handler.SlotHandler;
import net.unnamed.service.gui.api.inventory.Inventory;
import net.unnamed.service.gui.api.inventory.PlayerInventory;
import net.unnamed.service.gui.api.slot.Slot;
import org.antlr.v4.runtime.misc.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ContainerGui extends CommonGui {
    private final Inventory topInventory;
    private final PlayerInventory bottomInventory;

    public ContainerGui(Inventory topInventory, PlayerInventory bottomInventory) {
        this.topInventory = topInventory;
        this.bottomInventory = bottomInventory;
    }

    @Override
    public SlotHandler getSlotHandler() {
        return new SlotHandlerImpl(topInventory, bottomInventory);
    }

    public Inventory getBottomInventory() {
        return bottomInventory;
    }

    public Inventory getTopInventory() {
        return topInventory;
    }

    public static class SlotHandlerImpl implements SlotHandler {
        private final List<Map.Entry<Integer, Integer>> updatingSlots = new ArrayList<>();
        private final Map<Map.Entry<Integer, Integer>, Slot> slotCoords = new HashMap<>();
        private final Inventory topInventory;
        private final PlayerInventory bottomInventory;

        public SlotHandlerImpl(Inventory topInventory, PlayerInventory bottomInventory) {
            this.topInventory = topInventory;
            this.bottomInventory = bottomInventory;
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

        @Override
        public Pair<Integer, Inventory> findSlot(int x, int y) {
            int topRows = topInventory.getRows();
            int bottomRows = bottomInventory.getRows();

            if (y < topRows) {
                // In top inventory
                int slotIndex = y * topInventory.getColumns() + x;
                return new Pair<>(slotIndex, topInventory);
            } else {
                // In player inventory below
                int relativeY = y - topRows;
                if (relativeY < bottomRows) {
                    int slotIndex = relativeY * bottomInventory.getColumns() + x;
                    return new Pair<>(slotIndex, bottomInventory);
                } else {
                    // Invalid coordinate
                    return null;
                }
            }
        }
    }
}
