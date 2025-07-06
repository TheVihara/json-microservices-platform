package net.unnamed.service.gui.api.packet;

import net.unnamed.common.packet.Packet;
import java.util.UUID;

public class CloseGuiPacket extends Packet {
    public static final String ID = "close_gui";

    private UUID playerUuid;

    public CloseGuiPacket() {
        super(ID);
    }

    public CloseGuiPacket(UUID playerUuid) {
        super(ID);
        this.playerUuid = playerUuid;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public void setPlayerUuid(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }
}
