package net.unnamed.service.gui.api.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.unnamed.service.gui.api.InventoryViewer;
import net.unnamed.service.gui.api.handler.SlotHandler;
import net.unnamed.service.gui.api.inventory.ServiceInventory;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Getter
public abstract class BasicGui extends CommonGui {
    ServiceInventory inventory;

    @Override
    public SlotHandler getSlotHandler() {
        return inventory.getSlotHandler();
    }

    @Override
    public List<InventoryViewer> getViewers() {
        return inventory.getViewers();
    }

    @Override
    public void addViewer(InventoryViewer viewer) {
        inventory.addViewer(viewer);
    }

    @Override
    public void removeViewer(InventoryViewer viewer) {
        inventory.removeViewer(viewer);
    }

    @Override
    public Serializable<?> getSerializable() {
        return inventory.getSerializable();
    }
}
