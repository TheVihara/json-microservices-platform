package net.astopia.animationsystem;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.astopia.paperconnector.api.util.LocationUtil;
import net.unnamed.common.position.WorldPosition;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BlockEntityAttachment {
    UUID playerUUID;
    WorldPosition position;

    @NonFinal
    BlockDisplay display;

    @NonFinal
    int taskId;

    public void start(AnimationSystemPlugin plugin, Player player) {
        Location currentLocation = player.getLocation();
        Location targetLocation = LocationUtil.toLocation(position);
        BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
        World world = currentLocation.getWorld();

        display = world.spawn(currentLocation, BlockDisplay.class);
        display.setTeleportDuration(40);

        player.setGameMode(GameMode.SPECTATOR);
        player.setSpectatorTarget(display);

        bukkitScheduler.runTaskLater(plugin, () -> {
            display.teleport(targetLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
        }, 5L);

/*        taskId = bukkitScheduler.runTaskTimer(plugin, () -> {
            if (display == null || display.isDead() || !player.isOnline()) {
                bukkitScheduler.cancelTask(taskId);
                return;
            }

            Location newLocation = display.getLocation().add(0, 1.6, 0);
            player.teleport(newLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
        }, 0L, 1L).getTaskId();*/
    }
}
