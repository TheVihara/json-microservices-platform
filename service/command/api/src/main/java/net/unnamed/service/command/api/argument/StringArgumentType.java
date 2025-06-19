package net.unnamed.service.command.api.argument;

import net.unnamed.service.command.api.CommandSender;

import java.util.List;

public class StringArgumentType implements ArgumentType<String> {
    @Override
    public String parse(CommandSender sender, String input) {
        return input;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String input) {
        return List.of();
    }

    @Override
    public String getName() {
        return "string";
    }
}