package net.unnamed.minecraft.paper.essentials.home;

import net.unnamed.common.database.dao.DaoCrud;
import net.unnamed.minecraft.paper.essentials.player.EssentialsPlayer;
import org.bukkit.Location;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Home {
    private final Integer id;
    private final EssentialsPlayer owner;
    private final String name;
    private final Location location;

    public Home(Integer id, EssentialsPlayer owner, String name, Location location) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.location = location;
    }

    public static class Dao implements DaoCrud<Home, Integer> {
        @Override
        public CompletableFuture<Boolean> init() {
            return null;
        }

        @Override
        public CompletableFuture<Home> getById(Integer integer) {
            return null;
        }

        @Override
        public CompletableFuture<List<Home>> getAll() {
            return null;
        }

        @Override
        public CompletableFuture<Boolean> save(Home home) {
            return null;
        }

        @Override
        public CompletableFuture<Boolean> deleteById(Integer integer) {
            return null;
        }
    }
}
