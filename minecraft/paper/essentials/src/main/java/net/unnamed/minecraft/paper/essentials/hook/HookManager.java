package net.unnamed.minecraft.paper.essentials.hook;

import net.unnamed.minecraft.paper.essentials.player.PlayerManager;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;

public class HookManager {
    private final HashMap<String, Hook> hooks = new HashMap<>();

    public HookManager(PluginManager pluginManager, PlayerManager playerManager) {
        registerHook(new PlaceholderAPIHook(pluginManager, playerManager));
    }

    public void load() {
        hooks.values().forEach(Hook::load);
    }

    public void registerHook(Hook hook) {
        hooks.put(hook.getName(), hook);
    }

    public void unregisterHook(Hook hook) {
        hooks.remove(hook.getName());
    }

    public Hook getHook(String hook) {
        return hooks.get(hook);
    }
}
