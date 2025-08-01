package net.astopia.commandservice.api.packet;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.unnamed.common.packet.Packet;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
public class RegisterCommandPacket extends Packet {
    public static final String ID = "register_command";

    String command;
    List<String> aliases;
    String serviceContext;

    public RegisterCommandPacket() {
        super(ID);
    }

    public RegisterCommandPacket(String command, List<String> aliases, String serviceContext) {
        super(ID);
        this.command = command;
        this.aliases = aliases;
        this.serviceContext = serviceContext;
    }
}