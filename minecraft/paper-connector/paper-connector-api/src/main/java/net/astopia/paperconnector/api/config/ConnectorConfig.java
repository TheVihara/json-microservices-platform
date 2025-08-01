package net.astopia.paperconnector.api.config;

import de.bsommerfeld.jshepherd.annotation.Key;
import de.bsommerfeld.jshepherd.core.ConfigurablePojo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.unnamed.common.database.MySqlConfig;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ConnectorConfig extends ConfigurablePojo<ConnectorConfig> {
    @Key("server-name")
    String serverName = "default";

    @Key("mysql")
    MySqlConfig mySqlConfig = new MySqlConfig();
}
