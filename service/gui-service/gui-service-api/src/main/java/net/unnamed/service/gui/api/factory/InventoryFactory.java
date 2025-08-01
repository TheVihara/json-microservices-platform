package net.unnamed.service.gui.api.factory;

import net.kyori.adventure.text.Component;
import net.unnamed.service.gui.api.inventory.ChestServiceInventory;
import net.unnamed.service.gui.api.inventory.ServiceInventory;
import net.unnamed.service.gui.api.inventory.ServiceInventoryType;

public abstract class InventoryFactory<T extends InventoryFactory<T>> {
    private static InventoryFactory<?> instance;

    protected InventoryFactory() {}

    public static <T extends InventoryFactory<T>> void setInstance(T factory) {
        if (instance != null) {
            throw new IllegalStateException("InventoryFactory instance already set");
        }
        instance = factory;
    }

    @SuppressWarnings("unchecked")
    public static <T extends InventoryFactory<T>> T getInstance() {
        if (instance == null) {
            throw new IllegalStateException("InventoryFactory instance not set");
        }
        return (T) instance;
    }

    public abstract ServiceInventory createInventory(ServiceInventoryType type, Component title, int rows, int columns);
    public abstract ChestServiceInventory createChestInventory(Component title, int rows, int columns);
}
