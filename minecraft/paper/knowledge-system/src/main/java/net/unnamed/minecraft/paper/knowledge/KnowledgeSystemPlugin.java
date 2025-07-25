package net.unnamed.minecraft.paper.knowledge;

import net.unnamed.minecraft.paper.knowledge.manager.KnowledgeManager;
import org.bukkit.plugin.java.JavaPlugin;

public class KnowledgeSystemPlugin extends JavaPlugin {
    KnowledgeManager knowledgeManager = new KnowledgeManager(getDataFolder());

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
