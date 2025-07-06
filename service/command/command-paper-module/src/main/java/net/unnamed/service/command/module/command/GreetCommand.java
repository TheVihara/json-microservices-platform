package net.unnamed.service.command.module.command;

import net.unnamed.service.command.api.*;
import net.unnamed.service.command.api.annotation.*;
import java.util.Arrays;
import java.util.List;

public class GreetCommand {

    @Command(
            name = "greet",
            aliases = {"hello"},
            description = "Greet someone",
            usage = "/greet <name> [times]",
            cooldown = 5
    )
    public void greet(
            CommandContext context,
            @Arg(name = "name", suggestions = {"Alice", "Bob", "Charlie"}) String name,
            @Arg(name = "times", optional = true, defaultValue = "1") Integer times
    ) {
        for (int i = 0; i < times; i++) {
            context.getSender().sendMessage("Hello, " + name + "!");
        }
    }

    @Subcommand(
            name = "shout",
            description = "Shout a greeting",
            usage = "/greet shout <name>"
    )
    public void shout(
            CommandContext context,
            @Arg(name = "name") String name
    ) {
        context.getSender().sendMessage("HELLO, " + name.toUpperCase() + "!!!");
    }

    @TabComplete(command = "greet")
    public List<String> greetTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("Alice", "Bob", "Charlie");
        }
        return List.of();
    }
}