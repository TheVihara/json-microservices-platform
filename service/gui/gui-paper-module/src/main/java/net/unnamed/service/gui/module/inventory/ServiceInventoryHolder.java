package net.unnamed.service.gui.module.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class ServiceInventoryHolder implements InventoryHolder {
    private final Inventory inventory;

    public ServiceInventoryHolder(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
