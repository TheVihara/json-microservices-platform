package net.unnamed.service.command.api;

import java.util.HashMap;
import java.util.Map;

public class CommandContext {
    private final CommandSender sender;
    private final String commandName;
    private final String[] originalArgs;
    private final Map<String, Object> parsedArgs;
    private final Map<String, Boolean> flags;

    public CommandContext(CommandSender sender, String commandName, String[] originalArgs) {
        this.sender = sender;
        this.commandName = commandName;
        this.originalArgs = originalArgs;
        this.parsedArgs = new HashMap<>();
        this.flags = new HashMap<>();
    }

    public CommandSender getSender() { return sender; }
    public String getCommandName() { return commandName; }
    public String[] getOriginalArgs() { return originalArgs; }

    public <T> T getArg(String name) {
        return (T) parsedArgs.get(name);
    }

    public void setArg(String name, Object value) {
        parsedArgs.put(name, value);
    }

    public boolean hasFlag(String name) {
        return flags.getOrDefault(name, false);
    }

    public void setFlag(String name, boolean value) {
        flags.put(name, value);
    }
}