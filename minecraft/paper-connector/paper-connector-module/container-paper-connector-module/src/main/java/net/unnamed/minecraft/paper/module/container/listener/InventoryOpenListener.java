package net.unnamed.minecraft.paper.module.container.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.unnamed.minecraft.paper.module.container.ContainerConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import java.util.logging.Logger;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class InventoryOpenListener implements Listener {
    Logger logger = Logger.getLogger("InventoryOpenListener");
    ContainerConfig containerConfig;

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();

        if (inventory.getSize() > 36) {
            event.titleOverride(
                    MiniMessage.miniMessage().deserialize(
                            PlaceholderAPI.setPlaceholders(player, containerConfig.getLargeContainerTitle())
                    )
            );
        } else {
            event.titleOverride(
                    MiniMessage.miniMessage().deserialize(
                            PlaceholderAPI.setPlaceholders(player, containerConfig.getSmallContainerTitle())
                    )
            );
        }
    }
}
