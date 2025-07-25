package net.unnamed.service.gui.api.impl;

import net.unnamed.service.gui.api.Coords;
import net.unnamed.service.gui.api.InventoryViewer;
import net.unnamed.service.gui.api.handler.SlotHandler;
import net.unnamed.service.gui.api.inventory.ServiceInventory;
import net.unnamed.service.gui.api.inventory.PlayerServiceInventory;
import net.unnamed.service.gui.api.layer.InventoryLayer;
import net.unnamed.service.gui.api.slot.Slot;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ContainerGui extends CommonGui {
    private final ServiceInventory topServiceInventory;
    private final PlayerServiceInventory bottomInventory;

    public ContainerGui(ServiceInventory topServiceInventory, PlayerServiceInventory bottomInventory) {
        this.topServiceInventory = topServiceInventory;
        this.bottomInventory = bottomInventory;
    }

    @Override
    public SlotHandler getSlotHandler() {
        return new SlotHandlerImpl(topServiceInventory, bottomInventory);
    }

    public ServiceInventory getBottomInventory() {
        return bottomInventory;
    }

    public ServiceInventory getTopInventory() {
        return topServiceInventory;
    }

    public static class SlotHandlerImpl implements SlotHandler {
        private final List<Map.Entry<Integer, Integer>> updatingSlots = new ArrayList<>();
        private final Map<Map.Entry<Integer, Integer>, Slot> slotCoords = new HashMap<>();
        private final ServiceInventory topServiceInventory;
        private final PlayerServiceInventory bottomInventory;

        public SlotHandlerImpl(ServiceInventory topServiceInventory, PlayerServiceInventory bottomInventory) {
            this.topServiceInventory = topServiceInventory;
            this.bottomInventory = bottomInventory;
        }

/*        @Override
        public List<Map.Entry<Integer, Integer>> getUpdatingSlots() {
            return updatingSlots;
        }

        @Override
        public Map<Map.Entry<Integer, Integer>, Slot> getSlotCoords() {
            return slotCoords;
        }

        @Override
        public void setSlot(Slot slot) {
            slotCoords.put(slot.getCoords(), slot);
        }*/

        @Override
        public Map<String, InventoryLayer> getLayers() {
            return Map.of();
        }

        @Override
        public InventoryLayer getLayer(String key) {
            return null;
        }

        @Override
        public Slot getSlot(int x, int y) {
            return null;
        }

        @Override
        public Slot getSlot(Coords coords) {
            return null;
        }

/*        @Override
        public Slot getSlot(Map.Entry<Integer, Integer> slotCoords) {
            return null;
        }*/

        @Override
        public Slot getSlot(int index) {
            return null;
        }

        @Override
        public void addLayer(String key, InventoryLayer layer) {

        }

        @Override
        public void removeLayer(String key) {

        }

        @Override
        public void draw(InventoryViewer inventoryViewer) {

        }

/*        @Override
        public Pair<Integer, ServiceInventory> findSlot(int x, int y) {
            int topRows = topServiceInventory.getRows();
            int bottomRows = bottomInventory.getRows();

            if (y < topRows) {
                // In top inventory
                int slotIndex = y * topServiceInventory.getColumns() + x;
                return Pair.of(slotIndex, topServiceInventory);
            } else {
                // In player inventory below
                int relativeY = y - topRows;
                if (relativeY < bottomRows) {
                    int slotIndex = relativeY * bottomInventory.getColumns() + x;
                    return Pair.of(slotIndex, bottomInventory);
                } else {
                    // Invalid coordinate
                    return null;
                }
            }
        }*/
    }
}
