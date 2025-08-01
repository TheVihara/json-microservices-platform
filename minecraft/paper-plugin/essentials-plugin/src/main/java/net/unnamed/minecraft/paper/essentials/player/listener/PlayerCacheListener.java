package net.unnamed.minecraft.paper.essentials.player.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.unnamed.minecraft.paper.essentials.executor.ExecutorManager;
import net.unnamed.minecraft.paper.essentials.player.PlayerManager;
import net.unnamed.minecraft.paper.essentials.api.player.executor.PlayerLoadExecutor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PlayerCacheListener implements Listener {

    ExecutorManager executorManager;
    PlayerManager playerManager;

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        playerManager.getLoader().loadPlayer(event.getUniqueId()).thenAccept(loaded -> {
            executorManager.execute(PlayerLoadExecutor.class, loaded);
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        playerManager.getLoader().loadPlayer(player).thenAccept(loaded -> {
            if (loaded == null) {
                Bukkit.getLogger().warning("Failed to load player data for: " + player.getName());
                return;
            }

            event.joinMessage(null);
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        playerManager.getLoader().handleQuit(player);
    }
}
