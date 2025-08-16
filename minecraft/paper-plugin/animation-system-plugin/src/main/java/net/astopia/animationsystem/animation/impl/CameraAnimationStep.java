package net.astopia.animationsystem.animation.impl;

import net.astopia.animationsystem.animation.AnimationStep;
import org.bukkit.entity.Player;

public class CameraAnimationStep extends AnimationStep {
    public CameraAnimationStep(int id) {
        super(id);
    }

    @Override
    public void execute(Player player) {

    }

    @Override
    public boolean canStart(AnimationStep previousStep, Player player) {
        return false;
    }

    @Override
    public boolean hasEnded(Player player) {
        return false;
    }
}
