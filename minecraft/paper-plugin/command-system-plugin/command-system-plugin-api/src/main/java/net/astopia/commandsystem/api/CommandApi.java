package net.astopia.commandsystem.api;

import org.bukkit.Bukkit;

public interface CommandApi {
    void registerCommand(Object object);
    void disableCommand(String name);
    void enableCommand(String name);
    boolean isCommandEnabled(String name);

    static CommandApi getInstance() {
        return Bukkit.getServicesManager().load(CommandApi.class);
    }
}
