package net.unnamed.minecraft.paper.essentials.player;

import com.zaxxer.hikari.HikariDataSource;
import net.kyori.adventure.text.Component;
import net.unnamed.common.database.Result;
import net.unnamed.common.database.mysql.MySqlDatabase;
import net.unnamed.minecraft.paper.essentials.EssentialsPlugin;
import net.unnamed.minecraft.paper.essentials.EssentialsScheduler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class PlayerManager {
    private final EssentialsScheduler scheduler;
    private final EssentialsPlayer.Dao dao;
    private final ConcurrentHashMap<UUID, EssentialsPlayer> players = new ConcurrentHashMap<>();

    public PlayerManager(EssentialsScheduler scheduler,
                         HikariDataSource dataSource,
                         Consumer<Listener> registrar) {
        this.scheduler = scheduler;
        this.dao = new EssentialsPlayer.Dao(dataSource);

        registrar.accept(new CacheListener(this));
    }

    public EssentialsPlayer loadPlayer(final Player player) {
        if (players.containsKey(player.getUniqueId())) {
            final OfflineEssentialsPlayer essentialsPlayer = players.get(player.getUniqueId());

            if (essentialsPlayer.isOnline()) {
                return (EssentialsPlayer) essentialsPlayer;
            }

            final EssentialsPlayer newPlayer = new EssentialsPlayer(player);
            newPlayer.update(essentialsPlayer);
            players.put(player.getUniqueId(), newPlayer);

            return newPlayer;
        }

        final EssentialsPlayer essentialsPlayer = new EssentialsPlayer(player);
        scheduler.runAsync(() -> dao.save(essentialsPlayer));

        return essentialsPlayer;
    }

    public Optional<EssentialsPlayer> unloadPlayer(final Player player) {
        final EssentialsPlayer essentialsPlayer = players.get(player.getUniqueId());
        if (essentialsPlayer != null) {
            unloadPlayer(essentialsPlayer);
        }

        return Optional.ofNullable(essentialsPlayer);
    }

    public CompletableFuture<Result<Boolean>> asyncLoadPlayer(final UUID uuid) {
        return dao.getByUUID(uuid).thenApply(offlinePlayer -> {
            if (offlinePlayer != null) {
                players.put(uuid, offlinePlayer);
                return Result.success(true);
            } else {
                return Result.<Boolean>failure("Player not found in database.");
            }
        }).exceptionally(ex -> Result.failure("Exception: " + ex.getMessage()));
    }

    public CompletableFuture<Result<Boolean>> asyncLoadPlayer(final EssentialsPlayer player) {
        return dao.getByUUID(player.getUuid())
                .thenApply(offlinePlayer -> {
                    if (offlinePlayer != null) {
                        player.update(offlinePlayer);
                        return Result.success(true);
                    } else {
                        return Result.<Boolean>failure("Player not found in database.");
                    }
                }).exceptionally(ex -> Result.failure("Exception: " + ex.getMessage()));
    }


    public void unloadPlayer(final EssentialsPlayer player) {

    }

    public void addPlayer(final EssentialsPlayer player) {
        this.players.put(player.getUuid(), player);
    }

    public void removePlayer(final EssentialsPlayer player) {
        this.players.remove(player.getUuid());
    }

    public void removePlayer(final Player player) {
        this.players.remove(player.getUniqueId());
    }

    static class CacheListener implements Listener {
        private final PlayerManager playerManager;

        public CacheListener(PlayerManager playerManager) {
            this.playerManager = playerManager;
        }

        @EventHandler
        public void onLogin(AsyncPlayerPreLoginEvent event) {
            UUID uuid = event.getUniqueId();

            playerManager.asyncLoadPlayer(uuid).thenAccept((result) -> {
                if (result.success()) {
                    event.allow();
                } else {
                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text("Failed to load data, please contact an administrator."));
                }
            }).exceptionally(ex -> {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text(ex.getMessage()));
                return null;
            });
        }

        @EventHandler
        public void onJoin(PlayerJoinEvent event) {
            final Player player = event.getPlayer();

            playerManager.loadPlayer(player);
        }

        @EventHandler
        public void onQuit(PlayerQuitEvent event) {
            final Player player = event.getPlayer();
            final Optional<EssentialsPlayer> essentialsPlayer = playerManager.unloadPlayer(player);

            essentialsPlayer.ifPresent(playerManager::removePlayer);
        }
    }
}
