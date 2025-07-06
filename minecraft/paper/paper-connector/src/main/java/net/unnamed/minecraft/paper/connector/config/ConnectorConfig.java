package net.unnamed.minecraft.paper.connector.config;

import de.bsommerfeld.jshepherd.annotation.Key;
import de.bsommerfeld.jshepherd.core.ConfigurablePojo;

public class ConnectorConfig extends ConfigurablePojo<ConnectorConfig> {
    @Key("server-name")
    private String serverName = "default";

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerName() {
        return serverName;
    }
}
