package net.unnamed.minecraft.paper.essentials;

import de.bsommerfeld.jshepherd.core.ConfigurationLoader;
import net.milkbowl.vault.economy.Economy;
import net.unnamed.common.database.mysql.MySqlDatabase;
import net.unnamed.minecraft.paper.essentials.chat.ChatManager;
import net.unnamed.minecraft.paper.essentials.economy.VaultEconomyProvider;
import net.unnamed.minecraft.paper.essentials.hook.HookManager;
import net.unnamed.minecraft.paper.essentials.player.PlayerManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EssentialsPlugin extends JavaPlugin {
    private final EssentialsConfig config = ConfigurationLoader.load(getDataFolder().toPath().resolve("config.yaml"), EssentialsConfig::new, true);
    private final EssentialsScheduler scheduler = new EssentialsScheduler(this);
    private final MySqlDatabase database = new MySqlDatabase(config.getMySqlConfig());
    private final PluginManager pluginManager = getServer().getPluginManager();
    private final ServicesManager servicesManager = getServer().getServicesManager();
    private final PlayerManager playerManager = new PlayerManager(scheduler, database.getDataSource());
    private final ChatManager chatManager = new ChatManager(getDataFolder().toPath());
    private final HookManager hookManager = new HookManager(pluginManager);

    @Override
    public void onLoad() {
        servicesManager.register(Economy.class, new VaultEconomyProvider(playerManager), this, ServicePriority.Normal);
    }

    @Override
    public void onEnable() {
        chatManager.load(this::registerListener);
        playerManager.load(this::registerListener);
    }

    @Override
    public void onDisable() {

    }

    public void registerListener(Listener listener) {
        pluginManager.registerEvents(listener, this);
    }

    public EssentialsScheduler getScheduler() {
        return scheduler;
    }

    /*public MySqlDatabase getDatabase() {
        return database;
    }*/

    public PluginManager getPluginManager() {
        return pluginManager;
    }
}
