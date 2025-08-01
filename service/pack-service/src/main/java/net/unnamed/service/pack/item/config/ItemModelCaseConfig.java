package net.unnamed.service.pack.item.config;

import de.bsommerfeld.jshepherd.annotation.Key;
import de.bsommerfeld.jshepherd.core.ConfigurablePojo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ItemModelCaseConfig extends ConfigurablePojo<ItemModelCaseConfig> {
    @Key("when")
    List<String> when;

    @Key("model")
    ItemModelConfig model;
}
