package net.unnamed.minecraft.paper.essentials.player.cache;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.unnamed.common.database.Persisted;
import net.unnamed.minecraft.paper.essentials.api.player.EssentialsPlayer;
import net.unnamed.minecraft.paper.essentials.api.player.PlayerCacheApi;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlayerCache implements PlayerCacheApi {
    Map<UUID, Persisted<EssentialsPlayer>> players = new ConcurrentHashMap<>();

    public void put(EssentialsPlayer player, boolean added) {
        players.put(player.getUuid(), new Persisted<>(player, added));
    }

    @Override
    public Optional<EssentialsPlayer> getByUUID(UUID uuid) {
        return Optional.ofNullable(players.get(uuid)).map(Persisted::getInstance);
    }

    public Optional<EssentialsPlayer> getById(int id) {
        return players.values().stream()
                .map(Persisted::getInstance)
                .filter(p -> Objects.equals(p.getId(), id))
                .findFirst();
    }

    public Optional<EssentialsPlayer> getByName(String name) {
        return players.values().stream()
                .map(Persisted::getInstance)
                .filter(p -> p.getName() != null && p.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public void remove(UUID uuid) {
        players.remove(uuid);
    }

    public Collection<Persisted<EssentialsPlayer>> all() {
        return players.values();
    }
}
