package net.unnamed.service.gui.module.inventory;

import net.unnamed.service.gui.api.InventoryViewer;
import net.unnamed.service.gui.api.handler.SlotHandler;
import net.unnamed.service.gui.api.inventory.ServiceInventory;
import net.unnamed.service.gui.api.inventory.ServiceInventoryType;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class PaperInventory implements ServiceInventory {
    private final Inventory bukkitInventory;
    private final ServiceInventoryType type;
    private String title;
    private final int rows;
    private final int columns;

    public PaperInventory(ServiceInventoryType type, String title, int rows, int columns) {
        this.bukkitInventory = Bukkit.createInventory(new ServiceInventoryHolder(null), rows * columns);
        this.type = type;
        this.title = title;
        this.rows = rows;
        this.columns = columns;
    }

    @Override
    public String getTitle() {
        return title;
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
    public List<InventoryViewer> getViewers() {
        return List.of();
    }

    @Override
    public SlotHandler getSlotHandler() {
        return null;
    }

    @Override
    public Serializable<?> getSerializable() {
        return null;
    }

    @Override
    public void addViewer(InventoryViewer viewer) {

    }

    @Override
    public void removeViewer(InventoryViewer viewer) {

    }

    @Override
    public void draw() {

    }

    public Inventory getBukkitInventory() {
        return bukkitInventory;
    }
}
