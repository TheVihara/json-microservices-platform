package net.unnamed.service.gui.module.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.kyori.adventure.text.Component;
import net.unnamed.service.gui.api.item.Item;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@RequiredArgsConstructor
public class PaperItem implements Item {
    @NonFinal @NonNull ItemStack itemStack;

    @Override
    public Component getName() {
        return itemStack.getItemMeta().displayName();
    }

    @Override
    public String getMaterial() {
        return itemStack.getType().name();
    }

    @Override
    public List<Component> getLore() {
        return itemStack.getItemMeta().lore();
    }

    @Override
    public int getAmount() {
        return itemStack.getAmount();
    }

    @Override
    public int getCustomModelData() {
        return -1;
    }

    @Override
    public void setName(String name) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text(name));
        itemStack.setItemMeta(itemMeta);
    }

    @Override
    public void setMaterial(String material) {
        Material type = Material.valueOf(material);
        itemStack = new ItemStack(type);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.empty());
        itemMeta.lore(List.of());
        itemMeta.setCustomModelData(0);
        itemStack.setItemMeta(itemMeta);
    }

    @Override
    public void setLore(List<Component> lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);
    }

    @Override
    public void setAmount(int amount) {

    }

    @Override
    public void setCustomModelData(int customModelData) {

    }
}
