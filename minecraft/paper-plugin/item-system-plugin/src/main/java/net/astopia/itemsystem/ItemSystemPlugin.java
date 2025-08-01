package net.astopia.itemsystem;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.astopia.itemsystem.manager.ItemManager;
import org.bukkit.plugin.java.JavaPlugin;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemSystemPlugin extends JavaPlugin {
    ItemManager itemManager = new ItemManager();

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
