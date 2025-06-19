package net.unnamed.service.command.api.argument;

import net.unnamed.service.command.api.CommandSender;
import java.util.List;

public interface ArgumentType<T> {
    T parse(CommandSender sender, String input) throws ArgumentParseException;
    List<String> tabComplete(CommandSender sender, String input);
    String getName();
}