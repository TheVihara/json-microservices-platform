package net.unnamed.service.command.module;

import net.unnamed.service.command.api.AnnotationCommandRegistry;
import net.unnamed.service.command.api.CommandInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaperCommandManager {
    private final Logger logger;
    private final AnnotationCommandRegistry commandRegistry = new AnnotationCommandRegistry();
    private final Map<String, Command> registeredCommands = new ConcurrentHashMap<>();
    private CommandMap commandMap;

    public PaperCommandManager(Logger logger) {
        this.logger = logger;
        initializeCommandMap();
    }

    private void initializeCommandMap() {
        try {
/*            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);*/
            this.commandMap = Bukkit.getServer().getCommandMap();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to initialize CommandMap", e);
        }
    }

    public void registerRegistryCommand(Object commandHandler) {
        commandRegistry.registerCommands(commandHandler);
    }

    public void registerCommand(CommandInfo commandInfo) {
        if (commandMap == null) {
            logger.warning("CommandMap is null, cannot register command: " + commandInfo.getName());
            return;
        }

        try {
            // Create the command
            PaperCommand command = new PaperCommand(commandInfo, commandRegistry);

            // Register the command
            commandMap.register(commandInfo.getService(), command);

            // Store reference
            registeredCommands.put(commandInfo.getName(), command);

            // Register aliases
            if (commandInfo.getAliases() != null) {
                for (String alias : commandInfo.getAliases()) {
                    registeredCommands.put(alias, command);
                }
            }

            logger.info("Successfully registered Paper command: " + commandInfo.getName());

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to register Paper command: " + commandInfo.getName(), e);
        }
    }

    public void unregisterCommand(String commandName) {
        Command command = registeredCommands.remove(commandName);
        if (command != null && commandMap != null) {
            try {
                // Unregister from command map
                command.unregister(commandMap);

                // Remove from known commands
                Field knownCommandsField = commandMap.getClass().getDeclaredField("knownCommands");
                knownCommandsField.setAccessible(true);
                Map<String, Command> knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);

                knownCommands.remove(commandName);
                knownCommands.remove(command.getName());

                // Remove aliases
                if (command.getAliases() != null) {
                    for (String alias : command.getAliases()) {
                        knownCommands.remove(alias);
                        registeredCommands.remove(alias);
                    }
                }

                logger.info("Successfully unregistered Paper command: " + commandName);

            } catch (Exception e) {
                logger.log(Level.SEVERE, "Failed to unregister Paper command: " + commandName, e);
            }
        }
    }

    public void unregisterAllCommands() {
        registeredCommands.keySet().forEach(this::unregisterCommand);
    }
}