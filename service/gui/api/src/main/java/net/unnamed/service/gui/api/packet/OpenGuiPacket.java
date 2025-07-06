package net.unnamed.service.gui.api.packet;

import net.unnamed.common.packet.Packet;
import java.util.UUID;

public class OpenGuiPacket extends Packet {
    public static final String ID = "open_gui";

    private UUID playerUuid;
    private String playerName;
    private String guiData;
    private String serverContext;

    public OpenGuiPacket() {
        super(ID);
    }

    public OpenGuiPacket(UUID playerUuid, String playerName, String guiData, String serverContext) {
        super(ID);
        this.playerUuid = playerUuid;
        this.playerName = playerName;
        this.guiData = guiData;
        this.serverContext = serverContext;
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

    public String getGuiData() {
        return guiData;
    }

    public void setGuiData(String guiData) {
        this.guiData = guiData;
    }

    public String getServerContext() {
        return serverContext;
    }

    public void setServerContext(String serverContext) {
        this.serverContext = serverContext;
    }
}