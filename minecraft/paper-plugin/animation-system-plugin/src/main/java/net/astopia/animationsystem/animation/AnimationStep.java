package net.astopia.animationsystem.animation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Getter
public abstract class AnimationStep {
    int id;

    public abstract void execute(Player player);
    public abstract boolean canStart(AnimationStep previousStep, Player player);
    public abstract boolean hasEnded(Player player);
}
