package net.astopia.animationsystem.animation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.astopia.animationsystem.AnimationSystemPlugin;
import net.astopia.animationsystem.animation.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AnimationSession {
    Map<String, String> metadata = new HashMap<>();
    @NonFinal int currentStepId = 1;

    UUID playerUUID;

    @NonFinal int taskId;

    public void start(PlayerManager playerManager, AnimationSystemPlugin plugin, Animation animation, Player player) {
        Map<Integer, AnimationStep> steps = animation.getSteps();
        AnimationStep step = steps.get(currentStepId);
        AtomicReference<AnimationStep> atomicStep = new AtomicReference<>(step);

        if (!step.canStart(null, player)) {
            player.sendMessage("Can't start animation!");
            return;
        }

        step.execute(player);

        taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            AnimationStep currentStep = atomicStep.get();

            if (currentStep.getId() != currentStepId) {
                AnimationStep newStep = steps.get(currentStepId);
                atomicStep.set(newStep);
                currentStep = newStep;
            }

            run(playerManager, player, animation, currentStep, steps);
        }, 1L, 1L).getTaskId();
    }

    public void run(PlayerManager playerManager, Player player, Animation animation, AnimationStep currentStep, Map<Integer, AnimationStep> steps) {
        AnimationStep step = steps.get(currentStepId + 1);

        if (step == null) {
            animation.end(player);
            Bukkit.getScheduler().cancelTask(taskId);
            playerManager.endAnimation(playerUUID);
            return;
        }

        if (!step.canStart(currentStep, player)) {
            Bukkit.getScheduler().cancelTask(taskId);
            return;
        }

        this.currentStepId+=1;

        step.execute(player);
    }
}
