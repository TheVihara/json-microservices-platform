package net.astopia.animationsystem.animation.manager;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.astopia.animationsystem.AnimationSystemPlugin;
import net.astopia.animationsystem.animation.Animation;
import net.astopia.animationsystem.animation.AnimationSession;
import net.astopia.animationsystem.animation.factory.SessionFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PlayerManager {
    Map<UUID, AnimationSession> sessions = new ConcurrentHashMap<>();

    AnimationManager animationManager;
    SessionFactory sessionFactory;
    AnimationSystemPlugin plugin;

    public void startAnimation(UUID playerUUID, int animationID) {
        if (sessions.containsKey(playerUUID)) {
            return;
        }

        Animation animation = animationManager.getAnimation(animationID);

        if (animation == null) {
            return;
        }

        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) {
            return;
        }

        if (!animation.requirementsMet(player)) {
            player.sendMessage("Requirements not met!");
            return;
        }

        AnimationSession session = sessionFactory.createSession(player);
        sessions.put(playerUUID, session);
        session.start(this, plugin, animation, player);
    }

    public void endAnimation(UUID playerUUID) {
        sessions.remove(playerUUID);
    }
}
