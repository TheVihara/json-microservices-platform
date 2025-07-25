package net.unnamed.minecraft.paper.essentials.api;

import net.unnamed.minecraft.paper.essentials.api.executor.ExecutorApi;
import net.unnamed.minecraft.paper.essentials.api.player.PlayerApi;
import org.bukkit.Bukkit;

public interface EssentialsApi {
    PlayerApi getPlayerApi();
    ExecutorApi getExecutorApi();
    EssentialsScheduler getScheduler();

    static EssentialsApi getInstance() {
        return (EssentialsApi) Bukkit.getPluginManager().getPlugin("EssentialsA");
    }
}
