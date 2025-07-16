package net.unnamed.minecraft.paper.module.container;

import de.bsommerfeld.jshepherd.core.ConfigurationLoader;
import net.unnamed.minecraft.paper.connector.module.ConnectorModule;
import net.unnamed.minecraft.paper.module.container.listener.InventoryOpenListener;
import net.unnamed.minecraft.paper.module.container.papi.PaddingExpansion;
import org.bukkit.Bukkit;

import java.util.logging.Logger;

public class ContainerModule extends ConnectorModule {
    ContainerConfig containerConfig;

    @Override
    public void onEnable() {
        this.containerConfig = ConfigurationLoader.load(dataFolder.resolve("config.yml"), ContainerConfig::new);
        plugin.getServer().getPluginManager().registerEvents(new InventoryOpenListener(containerConfig), plugin);
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PaddingExpansion().register();
        }
    }

    @Override
    public void onDisable() {

    }

    public Logger getLogger() {
        return plugin.getLogger();
    }
}