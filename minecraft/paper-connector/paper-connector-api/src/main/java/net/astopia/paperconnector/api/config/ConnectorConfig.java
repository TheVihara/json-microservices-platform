package net.astopia.paperconnector.api.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.astopia.jackson.annotation.JsonProperty;
import net.unnamed.common.config.YamlConfig;
import net.unnamed.common.database.MySqlConfig;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ConnectorConfig extends YamlConfig<ConnectorConfig> {

    @JsonProperty("server-name")
    String serverName = "default";

    @JsonProperty("mysql")
    MySqlConfig mySqlConfig = new MySqlConfig();
}
