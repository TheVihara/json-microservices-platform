package net.unnamed.service.pack.item.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.unnamed.common.config.YamlConfig;
import net.unnamed.service.pack.item.ItemModelType;
import net.unnamed.service.pack.item.ItemProperty;

import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ItemModelConfig extends YamlConfig<ItemModelConfig> {

    ItemModelType type;

    String model;

    ItemProperty property;

    @JsonProperty("property-settings")
    Map<String, Object> propertySettings;

    @JsonProperty("fallback")
    ItemModelConfig fallbackModel;

    List<ItemModelCase> cases;
}
