package net.unnamed.service.command.api;

import java.util.List;

public interface TabCompleter {
    List<String> complete(CommandSender sender, String[] args);
}