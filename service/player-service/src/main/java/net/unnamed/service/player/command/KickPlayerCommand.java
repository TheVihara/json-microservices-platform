package net.unnamed.service.player.command;

import net.unnamed.service.command.api.CommandContext;
import net.unnamed.service.command.api.annotation.Arg;
import net.unnamed.service.command.api.annotation.Command;

public class KickPlayerCommand {
    @Command(
            name = "kick",
            aliases = {"kick-player"},
            description = "Kick a player",
            usage = "/kick <player>"
    )
    public void kickPlayer(
            CommandContext context,
            @Arg(name = "player", description = "Player name") String player
    ) {
        String target = player != null ? player : context.getSender().getName();
        context.getSender().sendMessage("Player " + target + " kicked");
    }
}
