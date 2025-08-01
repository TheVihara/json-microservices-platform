package net.astopia.knowledgesystem.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.astopia.knowledgesystem.manager.KnowledgeManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PlayerListener implements Listener {
    KnowledgeManager knowledgeManager;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        knowledgeManager.loadPlayerKnowledge(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        knowledgeManager.unloadPlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerCraft(CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (knowledgeManager.hasUnlocked(player.getUniqueId(), "crafting")) {
            return;
        }

        knowledgeManager.unlockKnowledge(player.getUniqueId(), "crafting");
    }
}
