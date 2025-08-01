package net.unnamed.service.pack.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.unnamed.service.pack.item.config.ItemModelConfig;
import net.unnamed.service.pack.item.factory.ItemFactory;
import team.unnamed.creative.item.ItemModel;

import java.util.function.Function;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public enum ItemModelType {
    MODEL(typeHolder -> typeHolder.factory.createItemDefaultModel(typeHolder.config)),
    SELECT(typeHolder -> typeHolder.factory.createSelectItemModel(typeHolder.config))
    ;

    Function<ItemModelTypeHolder, ItemModel> function;

    public ItemModel getModel(ItemModelConfig config, ItemFactory itemFactory) {
        return function.apply(new ItemModelTypeHolder(config, itemFactory));
    }

    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @RequiredArgsConstructor
    class ItemModelTypeHolder {
        ItemModelConfig config;
        ItemFactory factory;
    }
}
