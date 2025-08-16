package net.unnamed.minecraft.paper.essentials;

import net.astopia.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.unnamed.common.config.YamlConfig;
import net.unnamed.common.database.MySqlConfig;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class EssentialsConfig extends YamlConfig<EssentialsConfig> {

    @JsonProperty("mysql")
    MySqlConfig mySqlConfig = new MySqlConfig();
}
