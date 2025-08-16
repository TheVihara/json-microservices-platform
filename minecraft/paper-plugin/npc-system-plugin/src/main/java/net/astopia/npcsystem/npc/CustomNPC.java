package net.astopia.npcsystem.npc;

import net.astopia.jackson.annotation.JsonIgnore;
import net.astopia.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import net.astopia.npcsystem.trigger.NPCTrigger;
import net.citizensnpcs.api.npc.NPC;
import net.unnamed.common.config.YamlConfig;
import net.unnamed.common.position.WorldPosition;
import org.bukkit.entity.EntityType;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
public class CustomNPC extends YamlConfig<CustomNPC> {

    @JsonIgnore
    Map<UUID, Integer> clientNPCS;

    @JsonIgnore
    NPC npc;

    String name;

    EntityType type = EntityType.PLAYER;

    WorldPosition position;

    @JsonProperty("server-sided")
    boolean serverSided;

    List<NPCTrigger> triggers = Collections.emptyList();

    @Builder
    public CustomNPC(String name, EntityType type, WorldPosition position, boolean serverSided, List<NPCTrigger> triggers, NPC npc) {
        this.name = name;
        this.type = type != null ? type : EntityType.PLAYER;
        this.position = position;
        this.serverSided = serverSided;
        this.triggers = triggers != null ? triggers : Collections.emptyList();

        if (serverSided) {
            this.npc = npc;
            return;
        }

        this.clientNPCS = new ConcurrentHashMap<>();
    }
}
