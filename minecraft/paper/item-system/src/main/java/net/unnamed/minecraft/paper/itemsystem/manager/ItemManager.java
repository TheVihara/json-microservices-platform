package net.unnamed.minecraft.paper.itemsystem.manager;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.unnamed.minecraft.paper.itemsystem.api.CustomItem;
import net.unnamed.minecraft.paper.itemsystem.api.interact.Interactable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SuppressWarnings("UnstableApiUsage")
public class ItemManager {
    HashMap<String, CustomItem> items = new HashMap<>();

    public void register(CustomItem customItem) {
        items.put(customItem.key(), customItem);
    }

    public Interactable getInteractableItem(String key) {
        CustomItem customItem = items.get(key);

        if (customItem == null) {
            return null;
        }

        if (!(customItem instanceof Interactable)) {
            return null;
        }

        return (Interactable) customItem;
    }

    public Interactable getInteractableItem(ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }

        if (!itemStack.hasData(DataComponentTypes.CUSTOM_MODEL_DATA)) {
            return null;
        }

        CustomModelData customModelData = itemStack.getData(DataComponentTypes.CUSTOM_MODEL_DATA);

        if (customModelData == null) {
            return null;
        }

        String customItemKey = customModelData.strings().getFirst();

        return getInteractableItem(customItemKey);
    }
}
