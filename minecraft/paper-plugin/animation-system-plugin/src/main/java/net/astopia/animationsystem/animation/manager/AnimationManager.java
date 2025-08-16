package net.astopia.animationsystem.animation.manager;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.astopia.animationsystem.animation.Animation;

import java.util.HashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AnimationManager {
    Map<Integer, Animation> animations = new HashMap<>();

    public Animation getAnimation(int id) {
        return animations.get(id);
    }
}
