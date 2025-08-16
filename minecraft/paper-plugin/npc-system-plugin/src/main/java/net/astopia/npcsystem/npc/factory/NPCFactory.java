package net.astopia.npcsystem.npc.factory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.astopia.npcsystem.animation.factory.AnimationFactory;
import net.astopia.npcsystem.npc.CustomNPC;
import net.astopia.npcsystem.trigger.NPCTrigger;
import net.astopia.paperconnector.api.util.LocationUtil;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.TraitFactory;
import net.citizensnpcs.api.trait.trait.PlayerFilter;
import org.bukkit.Location;

import java.util.LinkedList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NPCFactory {
    NPCRegistry npcRegistry;
    TraitFactory traitFactory;
    AnimationFactory animationFactory;

    public NPC createNPC(CustomNPC customNPC) {
        Location location = LocationUtil.toLocation(
                customNPC.getPosition()
        );
        NPC npc = npcRegistry.createNPC(
                customNPC.getType(),
                customNPC.getName()
        );

        if (!customNPC.isServerSided()) {
            PlayerFilter playerFilter = traitFactory.getTrait(PlayerFilter.class);
            playerFilter.setAllowlist();

            npc.addTrait(playerFilter);
        } else {
            customNPC.setNpc(npc);
        }

        createNPCTriggers(customNPC);
        npc.spawn(location);
        return npc;
    }

    public void createNPCTriggers(CustomNPC customNPC) {
        List<NPCTrigger> triggers = customNPC.getTriggers();
        if (triggers == null) {
            return;
        }

        for (NPCTrigger trigger : triggers) {
            LinkedList<String> animationStepCommands = trigger.getAnimationStepCommands();

            if (animationStepCommands == null || animationStepCommands.isEmpty()) {
                continue;
            }

            trigger.setAnimationSteps(
                    animationFactory.createAnimationSteps(customNPC, animationStepCommands)
            );
        }
    }
}
