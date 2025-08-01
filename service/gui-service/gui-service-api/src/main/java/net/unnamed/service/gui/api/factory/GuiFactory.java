package net.unnamed.service.gui.api.factory;

import net.unnamed.service.gui.api.Gui;
import net.unnamed.service.gui.api.handler.SlotHandler;
import net.unnamed.service.gui.api.inventory.ServiceInventory;

public abstract class GuiFactory {
    private static GuiFactory instance;

    protected GuiFactory() {}

    public static void setInstance(GuiFactory factory) {
        if (GuiFactory.instance != null) {
            throw new IllegalStateException("GuiFactory instance already set");
        }
        GuiFactory.instance = factory;
    }

    public static GuiFactory getInstance() {
        if (instance == null) {
            throw new IllegalStateException("GuiFactory instance not set");
        }
        return instance;
    }

    public abstract Gui createGui(ServiceInventory serviceInventory, SlotHandler slotHandler);
}
