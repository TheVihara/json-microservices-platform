package net.unnamed.service.command.api.argument;

import net.unnamed.service.command.api.CommandSender;

import java.util.List;
import java.util.UUID;

public class UUIDArgumentType implements ArgumentType<UUID> {
    @Override
    public UUID parse(CommandSender sender, String input) throws ArgumentParseException {
        try {
            return UUID.fromString(input);
        } catch (IllegalArgumentException e) {
            throw new ArgumentParseException("'" + input + "' is not a valid UUID");
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String input) {
        return List.of();
    }

    @Override
    public String getName() {
        return "uuid";
    }
}