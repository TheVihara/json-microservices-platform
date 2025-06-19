package net.unnamed.service.command.api.packet;

import net.unnamed.common.packet.Packet;

public class ExecuteCommandPacket extends Packet {
    public static final String ID = "execute_command";

    private String senderName;
    private boolean console;
    private String command;
    private String[] args;

    public ExecuteCommandPacket() {
        super(ID);
    }

    public ExecuteCommandPacket(String senderName, boolean console, String command, String[] args) {
        super(ID);
        this.senderName = senderName;
        this.console = console;
        this.command = command;
        this.args = args;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setConsole(boolean console) {
        this.console = console;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public String getSenderName() {
        return senderName;
    }

    public boolean isConsole() {
        return console;
    }

    public String getCommand() {
        return command;
    }

    public String[] getArgs() {
        return args;
    }
}

