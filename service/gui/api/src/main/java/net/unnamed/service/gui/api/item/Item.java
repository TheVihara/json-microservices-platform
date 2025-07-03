package net.unnamed.service.gui.api.item;

import net.kyori.adventure.text.Component;

import java.util.List;

public interface Item {
    Component getName();
    String getMaterial();
    List<Component> getLore();
    int getAmount();
    int getCustomModelData();
    void setName(String name);
    void setMaterial(String material);
    void setLore(List<Component> lore);
    void setAmount(int amount);
    void setCustomModelData(int customModelData);
}
