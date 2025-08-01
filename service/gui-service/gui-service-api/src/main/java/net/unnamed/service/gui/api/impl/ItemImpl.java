package net.unnamed.service.gui.api.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.unnamed.service.gui.api.item.Item;

import java.util.List;
import java.util.stream.Collectors;

public class ItemImpl implements Item {
    private Component name;
    private String material;
    private List<Component> lore;
    private int amount;
    private int customModelData;

    public ItemImpl(String material, int amount) {
        this.material = material;
        this.amount = amount;
        this.name = Component.empty();
        this.lore = List.of();
        this.customModelData = 0;
    }

    public ItemImpl(Component name, String material, List<Component> lore, int amount, int customModelData) {
        this.name = name;
        this.material = material;
        this.lore = lore;
        this.amount = amount;
        this.customModelData = customModelData;
    }

    @Override
    public Component getName() {
        return name;
    }

    @Override
    public String getMaterial() {
        return material;
    }

    @Override
    public List<Component> getLore() {
        return lore;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public int getCustomModelData() {
        return customModelData;
    }

    @Override
    public void setName(String name) {
        this.name = Component.text(name);
    }

    @Override
    public void setMaterial(String material) {
        this.material = material;
    }

    @Override
    public void setLore(List<Component> lore) {
        this.lore = lore;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public void setCustomModelData(int customModelData) {
        this.customModelData = customModelData;
    }

    public JSONObject serialize() {
        JSONObject json = new JSONObject();
        json.put("name", GsonComponentSerializer.gson().serialize(name));
        json.put("material", material);
        json.put("amount", amount);
        json.put("customModelData", customModelData);

        JSONArray loreArray = new JSONArray();
        for (Component component : lore) {
            loreArray.add(GsonComponentSerializer.gson().serialize(component));
        }
        json.put("lore", loreArray);

        return json;
    }

    public static ItemImpl deserialize(JSONObject json) {
        Component name = GsonComponentSerializer.gson().deserialize(json.getString("name"));
        String material = json.getString("material");
        int amount = json.getIntValue("amount");
        int customModelData = json.getIntValue("customModelData");

        JSONArray loreArray = json.getJSONArray("lore");
        List<Component> lore = loreArray.stream()
                .map(obj -> GsonComponentSerializer.gson().deserialize((String) obj))
                .collect(Collectors.toList());

        return new ItemImpl(name, material, lore, amount, customModelData);
    }
}