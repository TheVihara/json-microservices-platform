package net.unnamed.service.command.api.packet;

import net.unnamed.common.packet.Packet;
import net.unnamed.service.command.api.CommandResponse;

import java.util.List;

public class CommandResponsePacket extends Packet {
    public static final String ID = "command_response";

    private CommandResponse response;
    private String message;

    public CommandResponsePacket() {
        super(ID);
    }

    public CommandResponsePacket(CommandResponse response, String message) {
        super(ID);
        this.response = response;
        this.message = message;
    }

    public void setResponse(CommandResponse response) {
        this.response = response;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CommandResponse getResponse() { return response; }
    public String getMessage() { return message; }
}