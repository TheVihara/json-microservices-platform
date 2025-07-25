package net.unnamed.minecraft.paper.essentials.api.player;

import java.util.Optional;
import java.util.UUID;

public interface PlayerCacheApi {
    Optional<EssentialsPlayer> getByUUID(UUID uuid);
}
