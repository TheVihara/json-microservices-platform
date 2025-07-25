package net.unnamed.service.gui.module.manager;

import com.alibaba.fastjson2.JSON;
import net.unnamed.service.gui.api.Gui;
import net.unnamed.service.gui.api.factory.InventoryFactory;
import net.unnamed.service.gui.api.impl.BasicGui;
import net.unnamed.service.gui.api.impl.ContainerGui;
import net.unnamed.service.gui.api.serializer.SerializerType;
import net.unnamed.service.gui.module.factory.PaperInventoryFactory;
import net.unnamed.service.gui.module.inventory.CustomInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@SuppressWarnings("unchecked")
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
        if (gui instanceof ContainerGui) {

        } else if (gui instanceof BasicGui basicGui) {
            CustomInventory customInventory = (CustomInventory) InventoryFactory.<PaperInventoryFactory>getInstance().createInventory(
                    basicGui.getInventory().getType(),
                    basicGui.getInventory().getTitle(),
                    basicGui.getInventory().getRows(),
                    basicGui.getInventory().getColumns()
            );

            player.openInventory(customInventory.getBukkitInventory());
        } else if (gui instanceof CustomInventory customInventory) {
            player.openInventory(customInventory.getBukkitInventory());
        }
    }
}
