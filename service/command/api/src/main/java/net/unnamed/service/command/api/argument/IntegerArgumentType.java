package net.unnamed.service.command.api.argument;

import net.unnamed.service.command.api.CommandSender;

import java.util.List;

public class IntegerArgumentType implements ArgumentType<Integer> {
    private final int min;
    private final int max;

    public IntegerArgumentType() {
        this(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public IntegerArgumentType(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Integer parse(CommandSender sender, String input) throws ArgumentParseException {
        try {
            int value = Integer.parseInt(input);
            if (value < min || value > max) {
                throw new ArgumentParseException("Number must be between " + min + " and " + max);
            }
            return value;
        } catch (NumberFormatException e) {
            throw new ArgumentParseException("'" + input + "' is not a valid number");
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String input) {
        return List.of();
    }

    @Override
    public String getName() {
        return "integer";
    }
}