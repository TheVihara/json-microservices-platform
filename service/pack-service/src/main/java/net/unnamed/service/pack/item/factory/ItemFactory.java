package net.unnamed.service.pack.item.factory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.key.Key;
import net.unnamed.service.pack.item.ItemModelType;
import net.unnamed.service.pack.item.ItemProperty;
import net.unnamed.service.pack.item.config.ItemModelCase;
import net.unnamed.service.pack.item.config.ItemModelConfig;
import team.unnamed.creative.item.Item;
import team.unnamed.creative.item.ItemModel;
import team.unnamed.creative.item.SelectItemModel;
import team.unnamed.creative.item.property.ItemStringProperty;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ItemFactory {
    public Item createItem(Key key, ItemModelConfig itemModelConfig) {
        return Item.item(key, createItemModel(itemModelConfig));
    }

    public ItemModel createItemModel(ItemModelConfig itemModelConfig) {
        ItemModelType type = itemModelConfig.getType();
        return type.getModel(itemModelConfig, this);
    }

    public ItemModel createItemDefaultModel(ItemModelConfig itemModelConfig) {
        return ItemModel.reference(Key.key(itemModelConfig.getModel()));
    }

    public SelectItemModel.Case createSelectItemCase(ItemModelCase itemModelCase) {
        List<String> when = itemModelCase.getWhen();
        ItemModelConfig itemModelConfig = itemModelCase.getModel();
        return SelectItemModel.Case._case(createItemModel(itemModelConfig), when);
    }

    public SelectItemModel createSelectItemModel(ItemModelConfig itemModelConfig) {
        ItemProperty itemProperty = itemModelConfig.getProperty();
        ItemStringProperty itemStringProperty = itemProperty.getStringProperty(itemModelConfig);
        List<SelectItemModel.Case> cases = itemModelConfig.getCases().stream().map(this::createSelectItemCase).toList();
        ItemModel fallbackModel = createItemModel(itemModelConfig.getFallbackModel());
        return ItemModel.select(itemStringProperty, cases, fallbackModel);
    }
}
