package net.astopia.npcsystem.command;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.astopia.npcsystem.npc.manager.NPCManager;
import net.astopia.paperconnector.api.util.LocationUtil;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.util.PlayerAnimation;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.unnamed.common.position.WorldPosition;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Default;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CustomNPCCommand {
    NPCManager npcManager;

    @Command("custom-npc")
    public void customNpc(CommandSourceStack stack) {
        CommandSender commandSender = stack.getSender();
        Player player = (Player) commandSender;


    }

    @Command("custom-npc create <name> [server-sided]")
    public void customNpcCreate(
            CommandSourceStack stack,
            @Argument("name") String name,
            @Argument(value = "server-sided") @Default("true") boolean serverSided
    ) {
        CommandSender commandSender = stack.getSender();
        Player player = (Player) commandSender;
        WorldPosition npcPosition = LocationUtil.toWorldPosition(
                player.getLocation()
        );
      /*  CustomNPC customNPC = CustomNPC.builder()
                .name(name)
                .position(npcPosition)
                .serverSided(serverSided)
                .build();
        NPC npc = npcManager.loadNPC(customNPC);

        npcManager.saveNPC(customNPC);

        if (!customNPC.isServerSided()) {
            npcManager.showNPC(npc, player);
        }

        player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Created a new custom NPC called<reset> " + name));*/
    }

    @Command("custom-npc animate <animation>")
    public void customNpcAnimate(CommandSourceStack stack, @Argument("animation") String animation) {
        CommandSender commandSender = stack.getSender();
        Player player = (Player) commandSender;
        PlayerAnimation playerAnimation = PlayerAnimation.valueOf(animation.toUpperCase());
        NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(commandSender);

        if (npc == null) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You must select a NPC!"));
            return;
        }

        Entity entity = npc.getEntity();
        if (entity instanceof Player target) {
            playerAnimation.play(target);
        } else {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>This NPC is not a player!"));
        }
    }
}
