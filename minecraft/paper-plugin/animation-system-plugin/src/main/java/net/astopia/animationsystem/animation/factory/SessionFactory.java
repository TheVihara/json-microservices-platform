package net.astopia.animationsystem.animation.factory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.astopia.animationsystem.animation.AnimationSession;
import org.bukkit.entity.Player;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SessionFactory {
    public AnimationSession createSession(Player player) {
        return new AnimationSession(player.getUniqueId());
    }
}
