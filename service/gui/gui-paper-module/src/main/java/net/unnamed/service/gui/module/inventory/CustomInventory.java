package net.unnamed.service.gui.module.inventory;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import net.unnamed.service.gui.api.Coords;
import net.unnamed.service.gui.api.InventoryViewer;
import net.unnamed.service.gui.api.handler.SlotHandler;
import net.unnamed.service.gui.api.impl.CommonGui;
import net.unnamed.service.gui.api.inventory.ServiceInventory;
import net.unnamed.service.gui.api.inventory.ServiceInventoryType;
import net.unnamed.service.gui.api.item.Item;
import net.unnamed.service.gui.api.layer.InventoryLayer;
import net.unnamed.service.gui.api.slot.Slot;
import net.unnamed.service.gui.module.item.PaperItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@Getter
public abstract class CustomInventory extends CommonGui implements ServiceInventory {
    Inventory bukkitInventory;
    ServiceInventoryType type;
    SlotHandler slotHandler;
    Component title;
    int rows;
    int columns;

    public CustomInventory(ServiceInventoryType type, Component title, int rows, int columns) {
        this.bukkitInventory = Bukkit.createInventory(new CustomInventoryHolder(this), rows * columns);
        this.type = type;
        this.slotHandler = SlotHandler.of(this);
        this.title = title;
        this.rows = rows;
        this.columns = columns;
    }

    @Override
    public ServiceInventoryType getType() {
        return type;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getColumns() {
        return columns;
    }

    @Override
    public SlotHandler getSlotHandler() {
        return slotHandler;
    }

    @Override
    public Serializable<?> getSerializable() {
        return null;
    }

    @Override
    public final void open(InventoryViewer inventoryViewer) {
        addViewer(inventoryViewer);
        onOpen(inventoryViewer);
    }

    @Override
    public final void close(InventoryViewer inventoryViewer) {
        onClose(inventoryViewer);
    }

    @Override
    public final void draw(InventoryViewer inventoryViewer) {
        onDraw(inventoryViewer);
        slotHandler.draw(inventoryViewer);
    }

    @Override
    public void draw(Slot slot) {
        Coords coords = slot.getCoords();
        Item item = slot.getItem();
        InventoryLayer layer = slot.getLayer();
        int index = layer.findSlot(coords, this);

        if (!slot.isVisible()) {
            bukkitInventory.setItem(index, null);
            onDraw(slot);
            return;
        }

        if (!(item instanceof PaperItem paperItem)) {
            return;
        }

        bukkitInventory.setItem(index, paperItem.getItemStack());
        onDraw(slot);
    }

    public abstract void onClose(InventoryViewer viewer);
    public abstract void onOpen(InventoryViewer viewer);
    public abstract void onDraw(InventoryViewer viewer);
    public abstract void onDraw(Slot slot);
}
