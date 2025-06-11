package net.unnamed.service.command;

import net.unnamed.command.api.proto.Command;

import java.util.HashMap;

public class CommandRegistry {
    private final HashMap<String, Command.CommandInfo> commands = new HashMap<>();

    public void registerCommand(Command.CommandInfo info) {
        commands.put(info.getName(), info);
    }

    public void removeCommand(String name) {
        commands.remove(name);
    }

    public void disableCommand(String name) {
        Command.CommandInfo info = commands.get(name);
        if (info != null) {
            info.toBuilder().setEnabled(false).build();
        }
    }

    public void enableCommand(String name) {
        Command.CommandInfo info = commands.get(name);
        if (info != null) {
            info.toBuilder().setEnabled(true).build();
        }
    }
}
