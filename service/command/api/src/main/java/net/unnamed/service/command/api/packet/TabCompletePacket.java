package net.unnamed.service.command.api.packet;

import net.unnamed.common.packet.Packet;
import net.unnamed.service.command.api.CommandSender;

public class TabCompletePacket extends Packet {
    public static final String ID = "tab_complete";

    private String command;
    private String[] args;
    private CommandSender sender;

    public TabCompletePacket() {
        super(ID);
    }

    public TabCompletePacket(CommandSender sender, String command, String[] args) {
        super(ID);
        this.sender = sender;
        this.command = command;
        this.args = args;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public void setSender(CommandSender sender) {
        this.sender = sender;
    }

    public CommandSender getSender() { return sender; }
    public String getCommand() { return command; }
    public String[] getArgs() { return args; }
}