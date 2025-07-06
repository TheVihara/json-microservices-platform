package net.unnamed.service.gui.module;

import net.unnamed.minecraft.paper.connector.module.ConnectorModule;
import net.unnamed.service.gui.module.factory.PaperGuiFactory;
import net.unnamed.service.gui.module.factory.PaperInventoryFactory;

public class GuiModule extends ConnectorModule {
    private final PaperGuiManager guiManager = new PaperGuiManager();
    private final PaperGuiFactory guiFactory = new PaperGuiFactory();
    private final PaperInventoryFactory inventoryFactory = new PaperInventoryFactory();

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    public PaperGuiManager getGuiManager() {
        return guiManager;
    }

    public PaperGuiFactory getGuiFactory() {
        return guiFactory;
    }

    public PaperInventoryFactory getInventoryFactory() {
        return inventoryFactory;
    }
}
