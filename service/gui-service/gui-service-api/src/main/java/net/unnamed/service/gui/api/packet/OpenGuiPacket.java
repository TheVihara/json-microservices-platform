package net.unnamed.service.gui.api.packet;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.unnamed.common.packet.Packet;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
public class OpenGuiPacket extends Packet {
    public static final String ID = "open_gui";

    UUID playerUuid;
    String playerName;
    String guiData;
    String serverContext;

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
}