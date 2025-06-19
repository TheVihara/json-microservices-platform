package net.unnamed.service.command.api.packet;

import net.unnamed.common.packet.Packet;

import java.util.List;

public class TabCompleteResponsePacket extends Packet {
    public static final String ID = "tab_complete_response";

    private List<String> completions;

    public TabCompleteResponsePacket() {
        super(ID);
    }

    public TabCompleteResponsePacket(List<String> completions) {
        super(ID);
        this.completions = completions;
    }

    public void setCompletions(List<String> completions) {
        this.completions = completions;
    }

    public List<String> getCompletions() { return completions; }
}