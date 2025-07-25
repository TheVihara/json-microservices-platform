package net.unnamed.service.gui.module.inventory;

import net.kyori.adventure.text.Component;
import net.unnamed.service.gui.api.InventoryViewer;
import net.unnamed.service.gui.api.inventory.ChestServiceInventory;
import net.unnamed.service.gui.api.inventory.ServiceInventoryType;
import net.unnamed.service.gui.api.slot.Slot;

public class PaperChestInventory extends CustomInventory implements ChestServiceInventory {
    public PaperChestInventory(Component title, int rows, int columns) {
        super(ServiceInventoryType.CHEST, title, rows, columns);
    }

    @Override
    public int getSize() {
        return rows * columns;
    }

    @Override
    public void onClose(InventoryViewer viewer) {

    }

    @Override
    public void onOpen(InventoryViewer viewer) {

    }

    @Override
    public void onDraw(InventoryViewer viewer) {

    }

    @Override
    public void onDraw(Slot slot) {

    }
}
