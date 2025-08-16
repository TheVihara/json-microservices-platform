package net.astopia.npcsystem.animation.steps;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.astopia.npcsystem.animation.AnimationStep;
import net.astopia.npcsystem.npc.CustomNPC;
import net.astopia.paperconnector.api.util.LocationUtil;
import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.npc.NPC;
import net.unnamed.common.position.WorldPosition;
import org.bukkit.entity.Player;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WalkAnimationStep extends AnimationStep {
    public static final String ID = "walk";

    WorldPosition toPosition;

    public WalkAnimationStep(CustomNPC customNPC, WorldPosition toPosition) {
        super(ID, customNPC);
        this.toPosition = toPosition;
    }

    @Override
    public void execute(Player player) {
        NPC npc = customNPC.getNpc();
        if (npc == null) {
            return;
        }

        Navigator navigator = npc.getNavigator();
        navigator.setTarget(LocationUtil.toLocation(toPosition));
    }

    @Override
    public boolean canStart() {
        return true;
    }

    @Override
    public boolean hasEnded() {
        NPC npc = customNPC.getNpc();
        if (npc == null) return true;
        return !npc.getNavigator().isNavigating();
    }

    public static class Factory implements AnimationStep.Factory {
        @Override
        public AnimationStep create(CustomNPC customNPC, List<String> args) {
            if (args.size() < 3) {
                throw new IllegalArgumentException("WalkAnimationStep requires at least 3 arguments (x y z)");
            }

            double x = Double.parseDouble(args.get(0));
            double y = Double.parseDouble(args.get(1));
            double z = Double.parseDouble(args.get(2));

            float yaw = 0f;
            float pitch = 0f;
            String worldName = null;

            if (args.size() >= 5) {
                yaw = Float.parseFloat(args.get(3));
                pitch = Float.parseFloat(args.get(4));
            }

            if (args.size() == 6) {
                worldName = args.get(5);
            }

            WorldPosition position = WorldPosition.builder()
                    .x(x)
                    .y(y)
                    .z(z)
                    .yaw(yaw)
                    .pitch(pitch)
                    .worldName(worldName)
                    .build();

            return new WalkAnimationStep(customNPC, position);
        }
    }
}
