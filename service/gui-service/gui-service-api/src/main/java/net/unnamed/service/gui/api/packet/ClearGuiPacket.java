package net.unnamed.service.gui.api.packet;

import net.unnamed.common.packet.Packet;
import java.util.UUID;

public class ClearGuiPacket extends Packet {
    public static final String ID = "clear_gui";

    private UUID playerUuid;

    public ClearGuiPacket() {
        super(ID);
    }

    public ClearGuiPacket(UUID playerUuid) {
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
