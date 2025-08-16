package net.astopia.npcsystem.npc.manager;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.astopia.npcsystem.NpcSystemPlugin;
import net.astopia.npcsystem.animation.factory.AnimationFactory;
import net.astopia.npcsystem.animation.manager.AnimationManager;
import net.astopia.npcsystem.npc.CustomNPC;
import net.astopia.npcsystem.npc.factory.NPCFactory;
import net.astopia.npcsystem.npc.listener.NPCListener;
import net.astopia.npcsystem.trigger.NPCTrigger;
import net.astopia.npcsystem.trigger.TriggerType;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.TraitFactory;
import net.citizensnpcs.api.trait.trait.PlayerFilter;
import net.unnamed.common.config.YamlConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NPCManager {
    NPCRegistry npcRegistry = CitizensAPI.getTemporaryNPCRegistry();
    TraitFactory traitFactory = CitizensAPI.getTraitFactory();
    AnimationFactory animationFactory = new AnimationFactory();
    NPCFactory npcFactory = new NPCFactory(npcRegistry, traitFactory, animationFactory);

    @Getter HashSet<CustomNPC> customNPCS = new HashSet<>();

    AnimationManager animationManager;
    File npcFolder;
    Path npcPath;

    public NPCManager(NpcSystemPlugin plugin) {
        Path dataPath = plugin.getDataPath();

        this.animationManager = new AnimationManager(plugin);
        this.npcFolder = dataPath.resolve("npcs").toFile();
        this.npcPath = npcFolder.toPath();

        if (!npcFolder.exists()) {
            npcFolder.mkdirs();
        }

        loadNPCS();
        plugin.getServer().getPluginManager().registerEvents(new NPCListener(this), plugin);
    }

    public void loadNPCS() {
        File[] files = npcFolder.listFiles();

        if (files == null || files.length == 0) {
            return;
        }

        customNPCS.clear();

        for (File file : files ) {
            CustomNPC customNPC = YamlConfig.loadSafe(
                    CustomNPC.class,
                    file.toPath(),
                    () -> CustomNPC.builder().build()
            );

            Bukkit.getLogger().info("Loaded NPC: " + file.toPath().toString());

            loadNPC(customNPC);
            customNPCS.add(customNPC);
        }
    }

    public void saveNPC(CustomNPC customNPC) {
        YamlConfig.loadSafe(
                CustomNPC.class,
                npcPath.resolve(customNPC.getName() + ".yml"),
                () -> customNPC
        );
    }

    public NPC loadNPC(CustomNPC customNPC) {
        return npcFactory.createNPC(customNPC);
    }

    public void showAllNPCS(Player player) {
        for (CustomNPC customNPC : customNPCS) {
            if (!customNPC.isServerSided()) {
                showNPC(customNPC, player);
                triggerNPC(TriggerType.ON_JOIN, customNPC, player);
            }
        }
    }

    public void showAllNPCS(UUID playerUUID) {
        for (CustomNPC customNPC : customNPCS) {
            if (!customNPC.isServerSided()) {
                showNPC(customNPC, playerUUID);
                triggerNPC(TriggerType.ON_JOIN, customNPC, playerUUID);
            }
        }
    }

    public void triggerNPC(TriggerType triggerType, CustomNPC customNPC, Player player) {
        for (NPCTrigger npcTrigger : customNPC.getTriggers()) {
            if (npcTrigger.getType() != triggerType) {
                continue;
            }
            triggerNPC(customNPC, npcTrigger, player);
        }
    }

    public void triggerNPC(TriggerType triggerType, CustomNPC customNPC, UUID playerUUID) {
        for (NPCTrigger npcTrigger : customNPC.getTriggers()) {
            if (npcTrigger.getType() != triggerType) {
                continue;
            }
            triggerNPC(customNPC, npcTrigger, playerUUID);
        }
    }

    public void triggerNPC(CustomNPC customNPC, NPCTrigger trigger, UUID playerUUID) {
        Player player = Bukkit.getPlayer(playerUUID);
        triggerNPC(customNPC, trigger, player);
    }

    public void triggerNPC(CustomNPC customNPC, NPCTrigger trigger, Player player) {
        if (player == null || !player.isOnline()) {
            return;
        }

        animationManager.executeAnimationSteps(player.getUniqueId(), trigger.getAnimationSteps());
    }

    public void showNPC(CustomNPC customNPC, Player player) {
        showNPC(customNPC, player.getUniqueId());
    }

    public void showNPC(CustomNPC customNPC, UUID uuid) {
        NPC npc = customNPC.getNpc();
        if (npc == null) {
            npc = npcFactory.createNPC(customNPC);
        }
        showNPC(npc, uuid);
    }

    public void showNPC(NPC npc, Player player) {
        showNPC(npc, player.getUniqueId());
    }

    public void showNPC(NPC npc, UUID playerUUID) {
        PlayerFilter playerFilter = npc.getTraitNullable(PlayerFilter.class);
        playerFilter.addPlayer(playerUUID);
        playerFilter.recalculate();
    }

    public void hideNPC(NPC npc, Player player) {
        hideNPC(npc, player.getUniqueId());
    }

    public void hideNPC(NPC npc, UUID playerUUID) {
        PlayerFilter playerFilter = npc.getTraitNullable(PlayerFilter.class);
        playerFilter.removePlayer(playerUUID);
        playerFilter.recalculate();
    }
}
