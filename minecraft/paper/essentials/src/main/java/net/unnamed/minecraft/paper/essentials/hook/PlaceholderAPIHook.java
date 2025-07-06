package net.unnamed.minecraft.paper.essentials.hook;

import org.bukkit.plugin.PluginManager;

public class PlaceholderAPIHook implements Hook {
    private final PluginManager pluginManager;

    public PlaceholderAPIHook(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @Override
    public String getName() {
        return "placeholderapi";
    }

    @Override
    public void load() {
        if (!pluginManager.isPluginEnabled("PlaceholderAPI")) {
        }
    }
}
