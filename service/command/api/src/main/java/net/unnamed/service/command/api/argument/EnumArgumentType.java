package net.unnamed.service.command.api.argument;

import net.unnamed.service.command.api.CommandSender;

import java.util.Arrays;
import java.util.List;

public class EnumArgumentType<T extends Enum<T>> implements ArgumentType<T> {
    private final Class<T> enumClass;

    public EnumArgumentType(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public T parse(CommandSender sender, String input) throws ArgumentParseException {
        try {
            return Enum.valueOf(enumClass, input.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ArgumentParseException("'" + input + "' is not a valid " + enumClass.getSimpleName());
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String input) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(e -> e.name().toLowerCase())
                .filter(name -> name.startsWith(input.toLowerCase()))
                .toList();
    }

    @Override
    public String getName() {
        return enumClass.getSimpleName().toLowerCase();
    }
}