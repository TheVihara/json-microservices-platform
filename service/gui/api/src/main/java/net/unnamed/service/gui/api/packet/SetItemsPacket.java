package net.unnamed.service.gui.api.packet;

import net.unnamed.common.packet.Packet;
import java.util.Map;
import java.util.UUID;

public class SetItemsPacket extends Packet {
    public static final String ID = "set_items";

    private UUID playerUuid;
    private Map<Integer, String> itemsData;

    public SetItemsPacket() {
        super(ID);
    }

    public SetItemsPacket(UUID playerUuid, Map<Integer, String> itemsData) {
        super(ID);
        this.playerUuid = playerUuid;
        this.itemsData = itemsData;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public void setPlayerUuid(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }

    public Map<Integer, String> getItemsData() {
        return itemsData;
    }

    public void setItemsData(Map<Integer, String> itemsData) {
        this.itemsData = itemsData;
    }
}
