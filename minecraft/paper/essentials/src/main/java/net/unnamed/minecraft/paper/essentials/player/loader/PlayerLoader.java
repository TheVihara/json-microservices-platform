package net.unnamed.minecraft.paper.essentials.player.loader;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.unnamed.common.database.Result;
import net.unnamed.minecraft.paper.essentials.api.EssentialsScheduler;
import net.unnamed.minecraft.paper.essentials.api.player.EssentialsPlayer;
import net.unnamed.minecraft.paper.essentials.player.cache.PlayerCache;
import net.unnamed.minecraft.paper.essentials.player.dao.EssentialsPlayerDao;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlayerLoader {
    EssentialsScheduler scheduler;
    EssentialsPlayerDao dao;
    PlayerCache cache;

    public PlayerLoader(EssentialsScheduler scheduler, HikariDataSource dataSource, PlayerCache cache) {
        this.scheduler = scheduler;
        this.dao = new EssentialsPlayerDao(dataSource);
        this.cache = cache;

        dao.init().thenAccept(success -> {
            if (!success) {
                throw new IllegalStateException("Failed to initialize EssentialsPlayerDao");
            }
            scheduler.runAsyncRepeating(this::flush, 20, 10 * 20);
        });
    }

    public CompletableFuture<EssentialsPlayer> loadPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        return dao.getByUUID(uuid).thenApply(dbPlayer -> {
            EssentialsPlayer p = dbPlayer != null ? dbPlayer : new EssentialsPlayer(player);
            p.setLastLogin(new Timestamp(System.currentTimeMillis()));
            p.setName(player.getName());
            p.setSocketAddress(player.getAddress());

            cache.put(p, dbPlayer == null);
            scheduler.runAsync(() -> dao.save(p));
            return p;
        });
    }

    public CompletableFuture<EssentialsPlayer> loadPlayer(UUID uuid) {
        return dao.getByUUID(uuid).thenApply(dbPlayer -> {
            if (dbPlayer != null) {
                dbPlayer.setLastLogin(new Timestamp(System.currentTimeMillis()));
                cache.put(dbPlayer, false);
                scheduler.runAsync(() -> dao.save(dbPlayer));
                return dbPlayer;
            }
            return null;
        }).exceptionally(ex -> {
            Bukkit.getLogger().severe("Error loading player " + uuid + ": " + ex.getMessage());
            return null;
        });
    }

    public void handleQuit(Player player) {
        UUID uuid = player.getUniqueId();
        cache.getByUUID(uuid).ifPresent(p -> {
            p.setLastLogin(new Timestamp(System.currentTimeMillis()));
            scheduler.runAsync(() -> dao.save(p));
            cache.remove(uuid);
        });
    }

    public CompletableFuture<Void> flush() {
        var ops = cache.all().stream()
                .filter(p -> p.isAdded() || p.isUpdated())
                .map(p -> {
                    p.setUpdated(false);
                    return dao.save(p.getInstance()).thenAccept(saved -> {
                        if (p.isAdded()) {
                            cache.put(saved, false);
                            p.setAdded(false);
                        }
                    });
                }).collect(Collectors.toList());

        return CompletableFuture.allOf(ops.toArray(new CompletableFuture[0]));
    }
}
