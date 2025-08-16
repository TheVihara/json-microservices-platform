package net.astopia.npcsystem.animation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.astopia.npcsystem.npc.CustomNPC;
import org.bukkit.entity.Player;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public abstract class AnimationStep {
    String name;

    protected CustomNPC customNPC;

    public abstract void execute(Player player);
    public abstract boolean canStart();
    public abstract boolean hasEnded();

    @FunctionalInterface
    public interface Factory {
        AnimationStep create(CustomNPC customNPC, List<String> args);
    }
}
