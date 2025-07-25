package net.unnamed.minecraft.paper.essentials.player;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.unnamed.minecraft.paper.essentials.api.EssentialsScheduler;
import net.unnamed.minecraft.paper.essentials.api.player.PlayerApi;
import net.unnamed.minecraft.paper.essentials.api.player.PlayerCacheApi;
import net.unnamed.minecraft.paper.essentials.executor.ExecutorManager;
import net.unnamed.minecraft.paper.essentials.player.cache.PlayerCache;
import net.unnamed.minecraft.paper.essentials.player.listener.PlayerCacheListener;
import net.unnamed.minecraft.paper.essentials.player.loader.PlayerLoader;
import org.bukkit.event.Listener;

import java.util.function.Consumer;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlayerManager implements PlayerApi {
    PlayerCache cache;
    PlayerLoader loader;

    public PlayerManager(EssentialsScheduler scheduler, HikariDataSource dataSource) {
        this.cache = new PlayerCache();
        this.loader = new PlayerLoader(scheduler, dataSource, cache);
    }

    public void load(Consumer<Listener> registrar, ExecutorManager executorManager) {
        registrar.accept(new PlayerCacheListener(executorManager, this));
    }

    @Override
    public PlayerCacheApi getPlayerCache() {
        return cache;
    }
}
