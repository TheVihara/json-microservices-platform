package net.unnamed.service.gui.factory;

import net.unnamed.service.gui.api.Gui;
import net.unnamed.service.gui.api.factory.GuiFactory;
import net.unnamed.service.gui.api.handler.SlotHandler;
import net.unnamed.service.gui.api.inventory.ServiceInventory;

public class ServiceGuiFactory extends GuiFactory {
    @Override
    public Gui createGui(ServiceInventory serviceInventory, SlotHandler slotHandler) {
        return null;
    }
}
