package net.unnamed.service.gui.api.packet;

import net.unnamed.common.packet.Packet;
import java.util.Map;
import java.util.UUID;

public class ClickGuiPacket extends Packet {
    public static final String ID = "click_gui";

    private UUID playerUuid;
    private int slot;
    private String clickType;
    private Map<String, Object> clickData;

    public ClickGuiPacket() {
        super(ID);
    }

    public ClickGuiPacket(UUID playerUuid, int slot, String clickType) {
        super(ID);
        this.playerUuid = playerUuid;
        this.slot = slot;
        this.clickType = clickType;
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

    public String getClickType() {
        return clickType;
    }

    public void setClickType(String clickType) {
        this.clickType = clickType;
    }

    public Map<String, Object> getClickData() {
        return clickData;
    }

    public void setClickData(Map<String, Object> clickData) {
        this.clickData = clickData;
    }
}
