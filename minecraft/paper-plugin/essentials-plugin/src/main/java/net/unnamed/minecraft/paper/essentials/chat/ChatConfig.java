package net.unnamed.minecraft.paper.essentials.chat;

import de.bsommerfeld.jshepherd.annotation.Key;
import de.bsommerfeld.jshepherd.core.ConfigurablePojo;

public class ChatConfig extends ConfigurablePojo<ChatConfig> {
    @Key("chat-format")
    private String format = "%player_name%: %message%";

    public String getFormat() {
        return format;
    }
}
