package net.unnamed.service.gui.api;

import net.unnamed.service.gui.api.handler.SlotHandler;

import java.util.List;

public interface Gui {
    List<InventoryViewer> getViewers();
    SlotHandler getSlotHandler();
    void addViewer(InventoryViewer viewer);
    void removeViewer(InventoryViewer viewer);
    void draw();
}
