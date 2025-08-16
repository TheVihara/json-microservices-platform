package net.astopia.animationsystem.animation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.astopia.animationsystem.requirement.Requirement;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Getter
public abstract class Animation {
    int id;
    Map<Integer, AnimationStep> steps;
    Set<Requirement> requirements;

    public boolean requirementsMet(Player player) {
        for (Requirement requirement : requirements) {
            if (!requirement.isMet(player)) {
                return false;
            }
        }

        return true;
    }

    public abstract void end(Player player);
}
