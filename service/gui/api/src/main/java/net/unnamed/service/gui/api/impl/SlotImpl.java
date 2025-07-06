package net.unnamed.service.gui.api.impl;

import com.alibaba.fastjson2.JSONObject;
import net.unnamed.service.gui.api.action.Click;
import net.unnamed.service.gui.api.inventory.ServiceInventory;
import net.unnamed.service.gui.api.item.Item;
import net.unnamed.service.gui.api.slot.Slot;

import java.util.AbstractMap;
import java.util.Map;

public class SlotImpl implements Slot {
    private final Map.Entry<Number, Number> coords;
    private final ServiceInventory serviceInventory;
    private Item item;

    public SlotImpl(int x, int y, ServiceInventory serviceInventory) {
        this.coords = new AbstractMap.SimpleEntry<>((Number) x, (Number) y);
        this.serviceInventory = serviceInventory;
    }

    public SlotImpl(Map.Entry<Number, Number> coords, ServiceInventory serviceInventory, Item item) {
        this.coords = coords;
        this.serviceInventory = serviceInventory;
        this.item = item;
    }

    @Override
    public Map.Entry<Number, Number> getCoords() {
        return coords;
    }

    @Override
    public ServiceInventory getInventory() {
        return serviceInventory;
    }

    @Override
    public Item getItem() {
        return item;
    }

    @Override
    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public void onClick(Click click) {
        // Default implementation - can be overridden
    }

    public JSONObject serialize() {
        JSONObject json = new JSONObject();
        json.put("x", coords.getKey());
        json.put("y", coords.getValue());
        if (item != null && item instanceof ItemImpl) {
            json.put("item", ((ItemImpl) item).serialize());
        }
        return json;
    }

    public static SlotImpl deserialize(JSONObject json, ServiceInventory serviceInventory) {
        int x = json.getIntValue("x");
        int y = json.getIntValue("y");

        SlotImpl slot = new SlotImpl(x, y, serviceInventory);

        if (json.containsKey("item") && json.getJSONObject("item") != null) {
            slot.setItem(ItemImpl.deserialize(json.getJSONObject("item")));
        }

        return slot;
    }
}