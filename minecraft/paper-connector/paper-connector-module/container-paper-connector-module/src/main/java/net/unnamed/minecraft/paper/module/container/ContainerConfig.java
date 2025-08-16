package net.unnamed.minecraft.paper.module.container;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.astopia.jackson.annotation.JsonProperty;
import net.unnamed.common.config.YamlConfig;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ContainerConfig extends YamlConfig<ContainerConfig> {

    @JsonProperty("large-container-title")
    String largeContainerTitle = "Chest";

    @JsonProperty("small-container-title")
    String smallContainerTitle = "Chest";
}
