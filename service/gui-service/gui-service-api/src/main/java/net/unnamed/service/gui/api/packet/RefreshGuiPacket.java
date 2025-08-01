package net.unnamed.service.gui.api.packet;

import net.unnamed.common.packet.Packet;
import java.util.UUID;

public class RefreshGuiPacket extends Packet {
    public static final String ID = "refresh_gui";

    private UUID playerUuid;
    private String playerName;
    private String guiType;
    private String guiData;

    public RefreshGuiPacket() {
        super(ID);
    }

    public RefreshGuiPacket(UUID playerUuid) {
        super(ID);
        this.playerUuid = playerUuid;
    }

    public RefreshGuiPacket(UUID playerUuid, String playerName, String guiType, String guiData) {
        super(ID);
        this.playerUuid = playerUuid;
        this.playerName = playerName;
        this.guiType = guiType;
        this.guiData = guiData;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public void setPlayerUuid(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getGuiType() {
        return guiType;
    }

    public void setGuiType(String guiType) {
        this.guiType = guiType;
    }

    public String getGuiData() {
        return guiData;
    }

    public void setGuiData(String guiData) {
        this.guiData = guiData;
    }
}
