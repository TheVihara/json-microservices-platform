package net.unnamed.service.gui.api.impl;

import net.unnamed.service.gui.api.Gui;
import net.unnamed.service.gui.api.InventoryViewer;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonGui implements Gui {
    protected List<InventoryViewer> viewers = new ArrayList<>();

    @Override
    public void addViewer(InventoryViewer viewer) {
        if (!viewers.contains(viewer)) {
            viewers.add(viewer);
        }
    }

    @Override
    public void removeViewer(InventoryViewer viewer) {
        viewers.remove(viewer);
    }

    @Override
    public List<InventoryViewer> getViewers() {
        return viewers;
    }
}
