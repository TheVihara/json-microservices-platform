package net.unnamed.service.gui.api.inventory;

import net.unnamed.service.gui.api.handler.CommonSlotHandler;
import net.unnamed.service.gui.api.handler.SlotHandler;
import org.antlr.v4.runtime.misc.Pair;

public interface ChestServiceInventory extends ServiceInventory {

    /**
     * Returns the total number of slots in the chest.
     * Implementing class must provide this.
     */
    int getSize();

    @Override
    default ServiceInventoryType getType() {
        return ServiceInventoryType.CHEST;
    }

    @Override
    default int getColumns() {
        return 9;
    }

    @Override
    default int getRows() {
        // Number of rows = size / columns
        return getSize() / getColumns();
    }

    @Override
    default SlotHandler getSlotHandler() {
        return new SlotHandlerImpl(this);
    }

    class SlotHandlerImpl extends CommonSlotHandler<ChestServiceInventory> {
        public SlotHandlerImpl(ChestServiceInventory openedInventory) {
            super(openedInventory);
        }

        @Override
        public Pair<Integer, ServiceInventory> findSlot(int x, int y) {
            /*
             * Convert (x, y) into slot index:
             * slotIndex = y * columns + x
             */
            int columns = openedInventory.getColumns();
            int slotIndex = y * columns + x;

            // Validate index if needed:
            if (slotIndex < 0 || slotIndex >= openedInventory.getSize()) {
                return null; // or throw exception
            }

            return new Pair<>(slotIndex, openedInventory);
        }
    }
}
