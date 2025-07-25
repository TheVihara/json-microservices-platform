package net.unnamed.service.gui.api.layer;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.experimental.SuperBuilder;
import net.unnamed.service.gui.api.Coords;
import net.unnamed.service.gui.api.InventoryViewer;
import net.unnamed.service.gui.api.handler.SlotHandler;
import net.unnamed.service.gui.api.inventory.ServiceInventory;
import net.unnamed.service.gui.api.slot.Slot;

import java.util.HashMap;
import java.util.Map;

@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Getter
public class SimpleInventoryLayer implements InventoryLayer {
    int weight;
    protected Coords firstCoords;
    protected Coords secondCoords;
    @Builder.Default
    Map<Coords, Slot> slots = new HashMap<>();

    @Builder.Default
    @NonFinal boolean visible = false;

    @Override
    public Slot getSlot(Coords coords) {
        return slots.get(coords);
    }

    @Override
    public Slot getSlot(int x, int y, ServiceInventory inventory) {
        return slots.get(Coords.of(x, y));
    }

    @Override
    public Slot getSlot(int index, ServiceInventory inventory) {
        int columns = inventory.getColumns();
        int x = index % columns;
        int y = index / columns;
        return slots.get(Coords.of(x, y));
    }

    @Override
    public void setSlot(int x, int y, Slot slot) {
        setSlot(Coords.of(x, y), slot);
    }

    @Override
    public void setSlot(Coords coords, Slot slot) {
        slots.put(coords, slot);
    }

    @Override
    public int findSlot(int x, int y, ServiceInventory inventory) {
        if (x < 0 || x >= inventory.getColumns() || y < 0 || y >= inventory.getRows()) {
            return -1;
        }
        return y * inventory.getColumns() + x;
    }

    @Override
    public int findSlot(Coords coords, ServiceInventory inventory) {
        return findSlot(coords.getX(), coords.getY(), inventory);
    }

    @Override
    public boolean containsCoords(Coords coords) {
        int x = coords.getX();
        int y = coords.getY();

        int x1 = Math.min(firstCoords.getX(), secondCoords.getX());
        int x2 = Math.max(firstCoords.getX(), secondCoords.getX());
        int y1 = Math.min(firstCoords.getY(), secondCoords.getY());
        int y2 = Math.max(firstCoords.getY(), secondCoords.getY());

        return x >= x1 && x <= x2 && y >= y1 && y <= y2;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void draw(InventoryViewer viewer, ServiceInventory inventory) {
        onDraw(viewer, inventory);
        SlotHandler slotHandler = inventory.getSlotHandler();
        System.out.println("Drawing layer " + weight + " with " + slots.size() + " slots");
        for (Slot slot : slots.values()) {
            Coords coords = slot.getCoords();
            int x = coords.getX();
            int y = coords.getY();
            Slot currentSlot = slotHandler.getSlot(x, y);

            System.out.println("Drawing slot " + x + " " + y + " slot " + slot.getItem().getMaterial());
            if (currentSlot == null) {
                inventory.draw(slot); // TODO: probably going to be an issue
                continue;
            }

            InventoryLayer currentLayer = currentSlot.getLayer();
            if (currentLayer.equals(this)) {
                inventory.draw(slot);
                continue;
            }

            if (weight > currentLayer.getWeight()) {
                inventory.draw(slot);
            }
        }
    }

    public void onDraw(InventoryViewer viewer, ServiceInventory inventory) {

    }
}
