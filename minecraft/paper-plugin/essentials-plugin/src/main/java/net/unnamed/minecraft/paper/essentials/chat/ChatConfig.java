package net.unnamed.minecraft.paper.essentials.chat;

import net.astopia.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.unnamed.common.config.YamlConfig;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ChatConfig extends YamlConfig<ChatConfig> {
    @JsonProperty("chat-format")
    String format = "%player_name%: %message%";
}
