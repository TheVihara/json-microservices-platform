package net.astopia.animationsystem.command;


import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.astopia.animationsystem.AnimationSystemPlugin;
import net.astopia.animationsystem.BlockEntityAttachment;
import net.astopia.animationsystem.animation.manager.AnimationManager;
import net.unnamed.common.position.WorldPosition;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AnimationCommand {
    AnimationManager animationManager;
    AnimationSystemPlugin plugin;

    @Command("animation execute <name>")
    public void animationExecute(
            CommandSourceStack stack,
            @Argument("name") String name
    ) {
        CommandSender commandSender = stack.getSender();
        Player player = (Player) commandSender;


    }

    @Command("animation transition <x> <y> <z> <yaw> <pitch>")
    public void transitionTo(
            CommandSourceStack stack,
            @Argument("x") double x,
            @Argument("y") double y,
            @Argument("z") double z,
            @Argument("yaw") float yaw,
            @Argument("pitch") float pitch
    ) {
        Player player = (Player) stack.getSender();
        Location location = player.getLocation();
        World world = location.getWorld();
        WorldPosition worldPosition = WorldPosition.builder()
                .x(x)
                .y(y)
                .z(z)
                .yaw(yaw)
                .pitch(pitch)
                .worldName(world.getName())
                .build();

        player.sendMessage("Transitioning to " + worldPosition.toString());
        new BlockEntityAttachment(player.getUniqueId(), worldPosition).start(plugin, player);
    }
}
