package net.unnamed.service.gui.module.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public record CustomInventoryHolder(CustomInventory customInventory) implements InventoryHolder {
    @Override
    public @NotNull Inventory getInventory() {
        return customInventory.getBukkitInventory();
    }
}
