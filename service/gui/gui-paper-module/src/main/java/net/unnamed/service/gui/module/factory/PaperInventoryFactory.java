package net.unnamed.service.gui.module.factory;

import net.unnamed.service.gui.api.factory.InventoryFactory;
import net.unnamed.service.gui.api.inventory.ChestServiceInventory;
import net.unnamed.service.gui.api.inventory.ServiceInventory;
import net.unnamed.service.gui.api.inventory.ServiceInventoryType;
import net.unnamed.service.gui.module.inventory.PaperInventory;

public class PaperInventoryFactory extends InventoryFactory {
    @Override
    public ServiceInventory createInventory(ServiceInventoryType type, String title, int rows, int columns) {
        return new PaperInventory(type, title, rows, columns);
    }

    @Override
    public ChestServiceInventory createChestInventory(String title, int rows, int columns) {
        return (ChestServiceInventory) new PaperInventory(ServiceInventoryType.CHEST, title, rows, columns);
    }
}
