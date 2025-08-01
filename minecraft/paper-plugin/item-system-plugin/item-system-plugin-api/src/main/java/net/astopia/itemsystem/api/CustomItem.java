package net.astopia.itemsystem.api;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
@SuppressWarnings("UnstableApiUsage")
@Getter
public class CustomItem {
    String key;
    Component displayName;
    List<Component> lore;
    Material material;

    public ItemStack toItemStack() {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.displayName(displayName);
        itemMeta.lore(lore);

        itemStack.setItemMeta(itemMeta);

        itemStack.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                .addString(key)
                .addFlag(true)
                .build());

        return itemStack;
    }
}
