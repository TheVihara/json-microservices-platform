package net.unnamed.service.command.api.argument;

import net.unnamed.service.command.api.CommandSender;
import java.util.List;

public class DoubleArgumentType implements ArgumentType<Double> {
    private final double min;
    private final double max;

    public DoubleArgumentType() {
        this(Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public DoubleArgumentType(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Double parse(CommandSender sender, String input) throws ArgumentParseException {
        try {
            double value = Double.parseDouble(input);
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
        return "double";
    }
}
