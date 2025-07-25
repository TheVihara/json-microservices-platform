package net.unnamed.service.gui.module;

import lombok.Getter;
import net.unnamed.minecraft.paper.connector.module.ConnectorModule;
import net.unnamed.service.gui.module.factory.PaperGuiFactory;
import net.unnamed.service.gui.module.factory.PaperInventoryFactory;
import net.unnamed.service.gui.module.listener.PaperGuiListener;
import net.unnamed.service.gui.module.manager.KnowledgeManager;
import net.unnamed.service.gui.module.manager.PaperGuiManager;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

@Getter
public class GuiModule extends ConnectorModule {
    private final PaperGuiManager guiManager = new PaperGuiManager();
    private final PaperGuiFactory guiFactory = new PaperGuiFactory();
    private final PaperInventoryFactory inventoryFactory = new PaperInventoryFactory();
    private KnowledgeManager knowledgeManager;
    private Listener paperGuiListener;

    @Override
    public void onEnable() {
        this.knowledgeManager = new KnowledgeManager(dataFolder.resolve("knowledge").toFile());
        this.paperGuiListener = new PaperGuiListener(guiManager, knowledgeManager);
        plugin.getServer().getPluginManager().registerEvents(paperGuiListener, plugin);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(paperGuiListener);
    }
}
