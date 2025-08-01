package net.astopia.guipaperplugin.api.gui;

import net.astopia.guipaperplugin.api.inventory.CustomInventory;
import net.unnamed.service.gui.api.Gui;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;

public interface GuiApi {
    void openGui(Player player, Gui gui);
    void setTitle(InventoryOpenEvent event, CustomInventory customInventory);

    static GuiApi getInstance() {
        return Bukkit.getServicesManager().load(GuiApi.class);
    }
}
