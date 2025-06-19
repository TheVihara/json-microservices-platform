package net.unnamed.service.command.api.packet;

import net.unnamed.common.packet.Packet;
import net.unnamed.service.command.api.CommandInfo;

public class RegisterCommandPacket extends Packet {
    public static final String ID = "register_command";

    private CommandInfo commandInfo;

    public RegisterCommandPacket() {
        super(ID);
    }

    public RegisterCommandPacket(CommandInfo commandInfo) {
        super(ID);
        this.commandInfo = commandInfo;
    }

    public void setCommandInfo(CommandInfo commandInfo) {
        this.commandInfo = commandInfo;
    }

    public CommandInfo getCommandInfo() {
        return commandInfo;
    }
}
