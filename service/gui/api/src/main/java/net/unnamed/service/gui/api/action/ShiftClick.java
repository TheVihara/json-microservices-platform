package net.unnamed.service.gui.api.action;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.unnamed.service.gui.api.InventoryViewer;
import net.unnamed.service.gui.api.inventory.ServiceInventory;
import net.unnamed.service.gui.api.slot.Slot;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Getter
@Setter
public class ShiftClick implements Click {
    InventoryViewer viewer;
    ClickType type;
    ServiceInventory inventory;
    Slot clickedSlot;
    @NonFinal boolean cancelled;
}
