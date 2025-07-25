package net.unnamed.minecraft.paper.essentials;

import de.bsommerfeld.jshepherd.core.ConfigurationLoader;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.milkbowl.vault.economy.Economy;
import net.unnamed.common.database.mysql.MySqlDatabase;
import net.unnamed.minecraft.paper.essentials.api.EssentialsApi;
import net.unnamed.minecraft.paper.essentials.api.EssentialsScheduler;
import net.unnamed.minecraft.paper.essentials.api.executor.ExecutorApi;
import net.unnamed.minecraft.paper.essentials.api.player.PlayerApi;
import net.unnamed.minecraft.paper.essentials.chat.ChatManager;
import net.unnamed.minecraft.paper.essentials.economy.VaultEconomyProvider;
import net.unnamed.minecraft.paper.essentials.executor.ExecutorManager;
import net.unnamed.minecraft.paper.essentials.hook.HookManager;
import net.unnamed.minecraft.paper.essentials.player.PlayerManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EssentialsPlugin extends JavaPlugin implements EssentialsApi {
    EssentialsConfig config = ConfigurationLoader.load(getDataFolder().toPath().resolve("config.yml"), EssentialsConfig::new, true);
    EssentialsScheduler scheduler = new EssentialsScheduler(this);
    MySqlDatabase database = new MySqlDatabase(config.getMySqlConfig());
    PluginManager pluginManager = getServer().getPluginManager();
    ServicesManager servicesManager = getServer().getServicesManager();
    ExecutorManager executorManager = new ExecutorManager();
    PlayerManager playerManager = new PlayerManager(scheduler, database.getDataSource());
    ChatManager chatManager = new ChatManager(getDataFolder().toPath());
    HookManager hookManager = new HookManager(pluginManager, playerManager);

    @Override
    public void onLoad() {
        servicesManager.register(Economy.class, new VaultEconomyProvider(playerManager), this, ServicePriority.Normal);
    }

    @Override
    public void onEnable() {
        chatManager.load(this::registerListener);
        playerManager.load(this::registerListener, executorManager);
        hookManager.load();
    }

    @Override
    public void onDisable() {

    }

    public void registerListener(Listener listener) {
        pluginManager.registerEvents(listener, this);
    }

    @Override
    public PlayerApi getPlayerApi() {
        return playerManager;
    }

    @Override
    public ExecutorApi getExecutorApi() {
        return executorManager;
    }

    @Override
    public EssentialsScheduler getScheduler() {
        return scheduler;
    }
}
