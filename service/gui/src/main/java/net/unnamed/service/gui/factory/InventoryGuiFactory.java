package net.unnamed.service.gui.factory;

import net.unnamed.service.gui.api.factory.InventoryFactory;
import net.unnamed.service.gui.api.inventory.ChestServiceInventory;
import net.unnamed.service.gui.api.inventory.ServiceInventory;
import net.unnamed.service.gui.api.inventory.ServiceInventoryType;

public class InventoryGuiFactory extends InventoryFactory {
    @Override
    public ServiceInventory createInventory(ServiceInventoryType type, String title, int rows, int columns) {
        return null;
    }

    @Override
    public ChestServiceInventory createChestInventory(String title, int rows, int columns) {
        return null;
    }
}
