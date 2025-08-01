package net.astopia.itemsystem.api.interact;

import org.bukkit.event.player.PlayerInteractEvent;

public interface Interactable {
    void onClick(PlayerInteractEvent event);
    void onRightClick(PlayerInteractEvent event);
    void onLeftClick(PlayerInteractEvent event);
    void onShiftRightClick(PlayerInteractEvent event);
    void onShiftLeftClick(PlayerInteractEvent event);
}
