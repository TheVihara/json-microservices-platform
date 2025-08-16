package net.astopia.npcsystem.npc.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.astopia.npcsystem.npc.manager.NPCManager;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NPCListener implements Listener {
    NPCManager npcManager;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        npcManager.showAllNPCS(player.getUniqueId());
    }

    @EventHandler
    public void onRightClick(NPCRightClickEvent event) {
        Player player = event.getClicker();
        if (player == null) {
            return;
        }

        NPC npc = event.getNPC();
        if (npc == null) {
            return;
        }

        String id = npc.getName();
        if (id == null) {
            return;
        }
    }


    @EventHandler
    public void onLeftClick(NPCLeftClickEvent event) {

    }
}
