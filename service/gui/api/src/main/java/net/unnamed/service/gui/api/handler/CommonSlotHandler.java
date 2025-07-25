package net.unnamed.service.gui.api.handler;

import lombok.Getter;
import lombok.Setter;
import net.unnamed.service.gui.api.Coords;
import net.unnamed.service.gui.api.InventoryViewer;
import net.unnamed.service.gui.api.inventory.ServiceInventory;
import net.unnamed.service.gui.api.layer.InventoryLayer;
import net.unnamed.service.gui.api.slot.Slot;

import java.util.*;

@Getter
@Setter
public abstract class CommonSlotHandler<T extends ServiceInventory> implements SlotHandler {
    private final Map<String, InventoryLayer> layers = new HashMap<>();
    protected final T openedInventory;

    public CommonSlotHandler(T openedInventory) {
        this.openedInventory = openedInventory;
    }

    @Override
    public Slot getSlot(int x, int y) {
        Coords coords = Coords.of(x, y);
        return getSlot(coords);
    }

    @Override
    public Slot getSlot(Coords coords) {
        Optional<InventoryLayer> optionalInventoryLayer = layers.values().stream()
                .filter((predicateLayer) -> predicateLayer.containsCoords(coords))
                .filter(InventoryLayer::isVisible).max(Comparator.comparingInt(InventoryLayer::getWeight));

        InventoryLayer inventoryLayer = optionalInventoryLayer.orElse(null);

        if (inventoryLayer == null) {
            return null;
        }

        return inventoryLayer.getSlot(coords);
    }

    @Override
    public Slot getSlot(int index) {
        int columns = openedInventory.getColumns();
        int x = index % columns;
        int y = index / columns;
        return getSlot(x, y);
    }

    @Override
    public void draw(InventoryViewer inventoryViewer) {
        List<InventoryLayer> visibleSortedLayers = layers.values().stream()
                .filter(InventoryLayer::isVisible)
                .sorted(Comparator.comparingInt(InventoryLayer::getWeight))
                .toList();

        for (InventoryLayer layer : visibleSortedLayers) {
            layer.draw(inventoryViewer, openedInventory);
        }

        Map<Coords, Slot> finalSlots = new HashMap<>();
        for (InventoryLayer layer : visibleSortedLayers) {
            finalSlots.putAll(layer.getSlots());
        }

        for (Map.Entry<Coords, Slot> entry : finalSlots.entrySet()) {
            Slot slot = entry.getValue();
            openedInventory.draw(slot);
        }
    }

    @Override
    public InventoryLayer getLayer(String key) {
        return layers.get(key);
    }

    @Override
    public void addLayer(String key, InventoryLayer layer) {
        layers.put(key, layer);
    }

    @Override
    public void removeLayer(String key) {
        layers.remove(key);
    }
}
