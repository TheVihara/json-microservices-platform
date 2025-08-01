package net.astopia.guipaperplugin.api;

import org.bukkit.Bukkit;

public interface GuiPluginApi {
    static GuiPluginApi getInstance() {
        return Bukkit.getServicesManager().load(GuiPluginApi.class);
    }
}
