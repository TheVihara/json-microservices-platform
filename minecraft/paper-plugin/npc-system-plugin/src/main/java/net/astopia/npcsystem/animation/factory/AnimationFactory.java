package net.astopia.npcsystem.animation.factory;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.astopia.npcsystem.animation.AnimationStep;
import net.astopia.npcsystem.animation.steps.WalkAnimationStep;
import net.astopia.npcsystem.npc.CustomNPC;
import net.unnamed.common.util.FastStrings;
import net.unnamed.common.util.ParsedBracketString;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AnimationFactory {
    Map<String, AnimationStep.Factory> animationStepFactories = new HashMap<>();

    public AnimationFactory() {
        animationStepFactories.put(WalkAnimationStep.ID, new WalkAnimationStep.Factory());
    }

    public LinkedList<AnimationStep> createAnimationSteps(CustomNPC customNPC, LinkedList<String> animationSteps) {
        LinkedList<AnimationStep> steps = new LinkedList<>();
        for (String step : animationSteps) {
            steps.add(createAnimationStep(customNPC, step));
        }
        return steps;
    }

    public AnimationStep createAnimationStep(CustomNPC customNPC, String step) {
        ParsedBracketString parsedBracketString = FastStrings.parseBracketString(step);
        String key = parsedBracketString.getKey();
        List<String> args = parsedBracketString.getArgs();

        AnimationStep.Factory animationStepFactory = animationStepFactories.get(key);
        if (animationStepFactory == null) {
            throw new IllegalArgumentException("Unknown animation step: " + key);
        }

        return animationStepFactory.create(customNPC, args);
    }
}
