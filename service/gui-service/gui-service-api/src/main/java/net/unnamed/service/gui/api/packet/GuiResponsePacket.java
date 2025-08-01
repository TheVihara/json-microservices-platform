package net.unnamed.service.gui.api.packet;

import net.unnamed.common.packet.Packet;

public class GuiResponsePacket extends Packet {
    public static final String ID = "gui_response";

    private boolean success;
    private String message;

    public GuiResponsePacket() {
        super(ID);
    }

    public GuiResponsePacket(boolean success, String message) {
        super(ID);
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
