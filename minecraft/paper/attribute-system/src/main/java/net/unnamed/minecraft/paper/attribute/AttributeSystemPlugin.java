package net.unnamed.minecraft.paper.attribute;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.unnamed.minecraft.paper.attribute.manager.AttributeManager;
import net.unnamed.minecraft.paper.essentials.api.EssentialsApi;
import org.bukkit.plugin.java.JavaPlugin;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttributeSystemPlugin extends JavaPlugin {

    EssentialsApi essentialsApi;
    AttributeManager attributeManager;

    @Override
    public void onEnable() {
        this.essentialsApi = EssentialsApi.getInstance();
        this.attributeManager = new AttributeManager(essentialsApi.getExecutorApi());

        attributeManager.load();
    }

    @Override
    public void onDisable() {

    }
}
