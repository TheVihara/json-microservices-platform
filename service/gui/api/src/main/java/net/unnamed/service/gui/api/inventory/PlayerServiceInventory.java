package net.unnamed.service.gui.api.inventory;

import net.unnamed.service.gui.api.handler.CommonSlotHandler;
import net.unnamed.service.gui.api.handler.SlotHandler;
import org.antlr.v4.runtime.misc.Pair;

public interface PlayerServiceInventory extends ServiceInventory {
    @Override
    default ServiceInventoryType getType() {
        return ServiceInventoryType.PLAYER;
    }

    @Override
    default int getRows() {
        return 4;
    }

    @Override
    default int getColumns() {
        return 9;
    }

    @Override
    default SlotHandler getSlotHandler() {
        return new SlotHandlerImpl(this);
    }

    class SlotHandlerImpl extends CommonSlotHandler<PlayerServiceInventory> {
        public SlotHandlerImpl(PlayerServiceInventory openedInventory) {
            super(openedInventory);
        }

        @Override
        public Pair<Integer, ServiceInventory> findSlot(int x, int y) {
            /*
             * Implement logic to convert (x, y) into slot index.
             * Example for player inventory (4 rows × 9 columns):
             * slotIndex = y * getColumns() + x;
             */
            int columns = openedInventory.getColumns();
            int slotIndex = y * columns + x;

            // Validate the slotIndex if needed
            return new Pair<>(slotIndex, openedInventory);
        }
    }
}
