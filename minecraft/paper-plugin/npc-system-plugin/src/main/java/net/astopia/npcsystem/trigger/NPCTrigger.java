package net.astopia.npcsystem.trigger;

import net.astopia.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.astopia.npcsystem.animation.AnimationStep;

import java.util.LinkedList;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class NPCTrigger {

    @JsonIgnore
    LinkedList<AnimationStep> animationSteps;

    TriggerType type;

    LinkedList<String> animationStepCommands;
}
