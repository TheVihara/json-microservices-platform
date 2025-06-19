package net.unnamed.service.command;

import net.unnamed.service.command.api.CommandInfo;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
    private final HashMap<String, CommandInfo> commands = new HashMap<>();
    private final HashMap<String, String> aliases = new HashMap<>();

    public CommandInfo getCommandInfo(String name) {
        CommandInfo info = commands.get(name);

        if (info != null) {
            return info;
        }

        for (Map.Entry<String, String> entry : aliases.entrySet()) {
            if (entry.getKey().equals(name)) {
                return commands.get(entry.getValue());
            }
        }

        return null;
    }

    public void registerCommand(CommandInfo info) {
        String name = info.getName();
        commands.put(name, info);
        for (String alias : info.getAliases()) {
            aliases.put(alias, name);
        }
    }

    public void removeCommand(String name) {
        commands.remove(name);
    }

    public void disableCommand(String name) {
        CommandInfo info = commands.get(name);
        if (info != null) {
            info.setEnabled(false);
        }
    }

    public void enableCommand(String name) {
        CommandInfo info = commands.get(name);
        if (info != null) {
            info.setEnabled(true);
        }
    }
}