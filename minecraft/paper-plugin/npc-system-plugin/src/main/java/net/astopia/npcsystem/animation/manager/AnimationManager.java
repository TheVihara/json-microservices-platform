package net.astopia.npcsystem.animation.manager;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.astopia.npcsystem.NpcSystemPlugin;
import net.astopia.npcsystem.animation.AnimationStep;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AnimationManager {

    NpcSystemPlugin plugin;

    Map<UUID, Deque<AnimationStep>> playerAnimations = new ConcurrentHashMap<>();
    Map<UUID, Set<AnimationStep>> startedSteps = new ConcurrentHashMap<>();
    Map<UUID, Integer> runningTaskIds = new ConcurrentHashMap<>();

    public void executeAnimationSteps(UUID playerUUID, Deque<AnimationStep> animationSteps) {
        if (animationSteps == null || animationSteps.isEmpty()) return;

        cancelAnimation(playerUUID);

        playerAnimations.put(playerUUID, animationSteps);
        startedSteps.put(playerUUID, Collections.newSetFromMap(new IdentityHashMap<>()));

        int taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null || !player.isOnline()) {
                cancelAnimation(playerUUID);
                return;
            }

            if (!pollAndRunSteps(player, animationSteps)) {
                cancelAnimation(playerUUID);
            }
        }, 0L, 1L).getTaskId();

        runningTaskIds.put(playerUUID, taskId);
    }

    private boolean pollAndRunSteps(Player player, Deque<AnimationStep> steps) {
        UUID uuid = player.getUniqueId();
        Set<AnimationStep> started = startedSteps.get(uuid);
        if (started == null || steps == null || steps.isEmpty()) return false;

        AnimationStep currentStep = steps.peek();
        if (currentStep == null) return false;

        if (!started.contains(currentStep)) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                currentStep.execute(player);
                started.add(currentStep);
            });
            return true;
        }

        while (!steps.isEmpty() && steps.peek().hasEnded()) {
            started.remove(steps.poll());
        }

        return !steps.isEmpty();
    }

    public void cancelAnimation(UUID playerUUID) {
        Integer taskId = runningTaskIds.remove(playerUUID);
        if (taskId != null) {
            Bukkit.getScheduler().cancelTask(taskId);
        }

        playerAnimations.remove(playerUUID);
        startedSteps.remove(playerUUID);
    }
}
