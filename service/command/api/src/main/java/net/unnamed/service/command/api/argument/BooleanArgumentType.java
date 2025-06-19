package net.unnamed.service.command.api.argument;

import net.unnamed.service.command.api.CommandSender;

import java.util.List;

public class BooleanArgumentType implements ArgumentType<Boolean> {
    @Override
    public Boolean parse(CommandSender sender, String input) throws ArgumentParseException {
        switch (input.toLowerCase()) {
            case "true", "yes", "on", "1" -> {
                return true;
            }
            case "false", "no", "off", "0" -> {
                return false;
            }
            default -> throw new ArgumentParseException("'" + input + "' is not a valid boolean");
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String input) {
        return List.of("true", "false", "yes", "no", "on", "off");
    }

    @Override
    public String getName() {
        return "boolean";
    }
}