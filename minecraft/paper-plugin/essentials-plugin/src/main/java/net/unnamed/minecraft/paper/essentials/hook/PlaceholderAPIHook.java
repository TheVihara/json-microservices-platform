package net.unnamed.minecraft.paper.essentials.hook;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.unnamed.minecraft.paper.essentials.papi.EssentialsExpansion;
import net.unnamed.minecraft.paper.essentials.player.PlayerManager;
import org.bukkit.plugin.PluginManager;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PlaceholderAPIHook implements Hook {
    PluginManager pluginManager;
    PlayerManager playerManager;

    @Override
    public String getName() {
        return "placeholderapi";
    }

    @Override
    public void load() {
        if (!pluginManager.isPluginEnabled("PlaceholderAPI")) {
            return;
        }

        new EssentialsExpansion(playerManager).register();
    }
}
