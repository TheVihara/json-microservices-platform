package net.unnamed.service.command.module;

import net.unnamed.minecraft.paper.connector.module.ConnectorModule;
import net.unnamed.service.command.api.CommandInfo;
import net.unnamed.service.command.module.command.GreetCommand;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandModule extends ConnectorModule {
    private PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        try {
            commandManager = new PaperCommandManager(getLogger());
            commandManager.registerRegistryCommand(new GreetCommand());
            commandManager.registerCommand(new CommandInfo("greet", List.of("kuracje"), "Kurac je", "none", true));
            plugin.getPacketRegistry().subscribe("command.packets");
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to enable CommandModule", e);
        }
    }

    @Override
    public void onDisable() {
        try {
            commandManager.unregisterAllCommands();

            getLogger().info("CommandModule disabled successfully");

        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Error during CommandModule disable", e);
        }
    }

    public PaperCommandManager getCommandManager() {
        return commandManager;
    }

    public Logger getLogger() {
        return plugin.getLogger();
    }
}