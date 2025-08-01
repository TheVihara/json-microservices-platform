package net.unnamed.service.pack.item.config;

import de.bsommerfeld.jshepherd.annotation.Key;
import de.bsommerfeld.jshepherd.core.ConfigurablePojo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.unnamed.service.pack.item.ItemModelType;
import net.unnamed.service.pack.item.ItemProperty;

import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ItemModelConfig extends ConfigurablePojo<ItemModelConfig> {
    @Key("type")
    ItemModelType type;

    @Key("model")
    String model;

    @Key("property")
    ItemProperty property;

    @Key("property-settings")
    Map<String, Object> propertySettings;

    @Key("fallback")
    ItemModelConfig fallbackModel;

    @Key("cases")
    List<ItemModelCaseConfig> cases;
}
