package net.unnamed.service.gui.module;

import com.alibaba.fastjson2.JSON;
import net.unnamed.service.gui.api.Gui;
import net.unnamed.service.gui.api.impl.ContainerGui;
import net.unnamed.service.gui.api.serializer.SerializerType;
import net.unnamed.service.gui.module.inventory.PaperInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.util.UUID;

public class PaperGuiManager {
    public void openGui(UUID uuid, String guiData) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return;
        }
        Gui gui = SerializerType.deserialize(JSON.parseObject(guiData));
        openGui(player, gui);
    }

    public void openGui(Player player, Gui gui) {
        InventoryView inventoryView;

        if (gui instanceof ContainerGui containerGui) {
            PaperInventory paperInventory = ((PaperInventory) containerGui.getTopInventory());
            Inventory bukkitInventory = paperInventory.getBukkitInventory();
            inventoryView = player.openInventory(bukkitInventory);
            inventoryView.setTitle(paperInventory.getTitle());
        }
    }
}
