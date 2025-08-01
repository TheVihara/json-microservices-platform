package net.astopia.itemsystem.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.astopia.itemsystem.api.interact.Interactable;
import net.astopia.itemsystem.manager.ItemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@SuppressWarnings({"UnstableApiUsage"})
public class ItemInteractListener implements Listener {
    ItemManager itemManager;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Interactable interactable = itemManager.getInteractableItem(event.getItem());

        if (interactable == null) {
            return;
        }

        Action action = event.getAction();
        boolean isShiftClick = event.getPlayer().isSneaking();

        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if (isShiftClick) {
                interactable.onShiftRightClick(event);
            } else {
                interactable.onRightClick(event);
            }
        } else if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            if (isShiftClick) {
                interactable.onShiftLeftClick(event);
            } else {
                interactable.onLeftClick(event);
            }
        }

        interactable.onClick(event);
    }
}
