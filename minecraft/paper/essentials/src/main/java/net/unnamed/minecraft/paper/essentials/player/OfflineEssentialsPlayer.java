package net.unnamed.minecraft.paper.essentials.player;

import java.util.UUID;

public class OfflineEssentialsPlayer {
    protected Integer id;
    protected UUID uuid;
    protected String name;
    
    public OfflineEssentialsPlayer(Integer id, UUID uuid, String name) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }
    
    public boolean isOnline() {
        return false;
    }
}
