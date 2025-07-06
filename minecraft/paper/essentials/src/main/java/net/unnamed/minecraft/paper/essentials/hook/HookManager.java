package net.unnamed.minecraft.paper.essentials.hook;

import org.bukkit.plugin.PluginManager;

import java.util.HashMap;

public class HookManager {
    private final HashMap<String, Hook> hooks = new HashMap<>();

    public HookManager(PluginManager pluginManager) {
        registerHook(new PlaceholderAPIHook(pluginManager));
    }

    public void registerHook(Hook hook) {
        hooks.put(hook.getName(), hook);
        hook.load();
    }

    public void unregisterHook(Hook hook) {
        hooks.remove(hook.getName());
    }

    public Hook getHook(String hook) {
        return hooks.get(hook);
    }
}
