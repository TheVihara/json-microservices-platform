package net.unnamed.service.common.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.unnamed.common.config.YamlConfig;

@Setter
@Getter
public class ServiceConfig extends YamlConfig<ServiceConfig> {

    String name;

    String description;

    @JsonProperty("main-class")
    String mainClass;
}
