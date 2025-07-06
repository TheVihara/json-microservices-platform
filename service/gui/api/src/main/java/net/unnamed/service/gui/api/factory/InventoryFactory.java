package net.unnamed.service.gui.api.factory;

import net.unnamed.service.gui.api.inventory.ChestServiceInventory;
import net.unnamed.service.gui.api.inventory.ServiceInventory;
import net.unnamed.service.gui.api.inventory.ServiceInventoryType;

public abstract class InventoryFactory {
    private static InventoryFactory instance;

    protected InventoryFactory() {}

    public static void setInstance(InventoryFactory factory) {
        if (InventoryFactory.instance != null) {
            throw new IllegalStateException("InventoryFactory instance already set");
        }
        InventoryFactory.instance = factory;
    }

    public static InventoryFactory getInstance() {
        if (instance == null) {
            throw new IllegalStateException("InventoryFactory instance not set");
        }
        return instance;
    }

    public abstract ServiceInventory createInventory(ServiceInventoryType type, String title, int rows, int columns);
    public abstract ChestServiceInventory createChestInventory(String title, int rows, int columns);
}
