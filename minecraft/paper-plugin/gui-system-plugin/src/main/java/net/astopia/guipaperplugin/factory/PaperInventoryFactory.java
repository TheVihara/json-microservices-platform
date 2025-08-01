package net.astopia.guipaperplugin.factory;

import net.kyori.adventure.text.Component;
import net.unnamed.service.gui.api.factory.InventoryFactory;
import net.unnamed.service.gui.api.inventory.ChestServiceInventory;
import net.unnamed.service.gui.api.inventory.ServiceInventory;
import net.unnamed.service.gui.api.inventory.ServiceInventoryType;
import net.astopia.guipaperplugin.api.inventory.CommonCustomInventory;
import net.astopia.guipaperplugin.api.inventory.PaperChestInventory;

public class PaperInventoryFactory extends InventoryFactory {
    @Override
    public ServiceInventory createInventory(ServiceInventoryType type, Component title, int rows, int columns) {
        switch (type) {
            case CHEST -> {
                return createChestInventory(title, rows, columns);
            }
        }
        return new CommonCustomInventory(type, title, rows, columns);
    }

    @Override
    public ChestServiceInventory createChestInventory(Component title, int rows, int columns) {
        return new PaperChestInventory(title, rows, columns);
    }
}
