package net.unnamed.service.common.config;

import de.bsommerfeld.jshepherd.annotation.Key;
import de.bsommerfeld.jshepherd.core.ConfigurablePojo;

public class ServiceConfig extends ConfigurablePojo<ServiceConfig> {
    @Key("name")
    private String name;

    @Key("description")
    private String description;

    @Key("main-class")
    private String mainClass;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getMainClass() {
        return mainClass;
    }
}
