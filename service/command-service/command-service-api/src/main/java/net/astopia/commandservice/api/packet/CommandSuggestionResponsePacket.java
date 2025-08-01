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
public class CommandSuggestionResponsePacket extends Packet {
    public static final String ID = "command_suggestion_response";

    List<String> suggestions;

    public CommandSuggestionResponsePacket() {
        super(ID);
    }

    public CommandSuggestionResponsePacket(List<String> suggestions) {
        super(ID);
        this.suggestions = suggestions;
    }
}
