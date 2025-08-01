package net.unnamed.service.pack.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.unnamed.service.pack.item.config.ItemModelConfig;
import team.unnamed.creative.item.property.ItemStringProperty;

import java.util.Map;
import java.util.function.Function;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public enum ItemProperty {
    CUSTOM_MODEL_DATA(config -> ItemStringProperty.customModelData()),
    CUSTOM_MODEL_DATA_INDEX(config -> {
        Map<String, Object> propertySettings = config.getPropertySettings();
        return ItemStringProperty.customModelData((Integer) propertySettings.get("custom-model-data"));
    });

    Function<ItemModelConfig, ItemStringProperty> function;

    public ItemStringProperty getStringProperty(ItemModelConfig config) {
        return function.apply(config);
    }
}
