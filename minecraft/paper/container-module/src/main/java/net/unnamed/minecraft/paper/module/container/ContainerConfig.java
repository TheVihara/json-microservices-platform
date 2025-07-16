package net.unnamed.minecraft.paper.module.container;

import de.bsommerfeld.jshepherd.annotation.Key;
import de.bsommerfeld.jshepherd.core.ConfigurablePojo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ContainerConfig extends ConfigurablePojo<ContainerConfig> {
    @Key("large-container-title")
    String largeContainerTitle = "Chest";

    @Key("small-container-title")
    String smallContainerTitle = "Chest";
}
