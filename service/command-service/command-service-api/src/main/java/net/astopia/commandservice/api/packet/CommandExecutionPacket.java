package net.astopia.commandservice.api.packet;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.unnamed.common.packet.Packet;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
public class CommandExecutionPacket extends Packet {
    public static final String ID = "command_execution";

    UUID senderUuid;
    String senderName;
    boolean isConsole;
    String commandInput;
    String serviceContext;

    public CommandExecutionPacket() {
        super(ID);
    }

    public CommandExecutionPacket(
            UUID senderUuid,
            String senderName,
            boolean isConsole,
            String commandInput,
            String serviceContext
    ) {
        super(ID);
        this.senderUuid = senderUuid;
        this.senderName = senderName;
        this.isConsole = isConsole;
        this.commandInput = commandInput;
        this.serviceContext = serviceContext;;
    }
}