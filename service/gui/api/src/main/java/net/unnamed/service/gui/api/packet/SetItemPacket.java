package net.unnamed.service.gui.api.packet;

import net.unnamed.common.packet.Packet;
import java.util.UUID;

public class SetItemPacket extends Packet {
    public static final String ID = "set_item";

    private UUID playerUuid;
    private int slot;
    private String itemData;

    public SetItemPacket() {
        super(ID);
    }

    public SetItemPacket(UUID playerUuid, int slot, String itemData) {
        super(ID);
        this.playerUuid = playerUuid;
        this.slot = slot;
        this.itemData = itemData;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public void setPlayerUuid(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public String getItemData() {
        return itemData;
    }

    public void setItemData(String itemData) {
        this.itemData = itemData;
    }
}
