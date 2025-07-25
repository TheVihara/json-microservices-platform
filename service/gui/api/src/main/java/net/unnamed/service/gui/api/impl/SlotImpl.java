package net.unnamed.service.gui.api.impl;

import com.alibaba.fastjson2.JSONObject;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.unnamed.service.gui.api.Coords;
import net.unnamed.service.gui.api.action.Click;
import net.unnamed.service.gui.api.action.DefaultClick;
import net.unnamed.service.gui.api.inventory.ServiceInventory;
import net.unnamed.service.gui.api.item.Item;
import net.unnamed.service.gui.api.layer.InventoryLayer;
import net.unnamed.service.gui.api.slot.Slot;

import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Consumer;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SlotImpl implements Slot {
    Coords coords;
    @NonFinal InventoryLayer layer;
    ServiceInventory inventory;
    @NonFinal Item item;
    @NonFinal Consumer<Click> clickConsumer;
    @NonFinal boolean visible = true;

    public SlotImpl(int x, int y, InventoryLayer layer, ServiceInventory inventory) {
        this.coords = Coords.of(x, y);
        this.layer = layer;
        this.inventory = inventory;
    }

    public SlotImpl(int x, int y, InventoryLayer layer, ServiceInventory inventory, Item item) {
        this.coords = Coords.of(x, y);
        this.layer = layer;
        this.inventory = inventory;
        this.item = item;
    }

    public SlotImpl(int x, int y, InventoryLayer layer, ServiceInventory inventory, Item item, Consumer<Click> clickConsumer) {
        this.coords = Coords.of(x, y);
        this.layer = layer;
        this.inventory = inventory;
        this.item = item;
        this.clickConsumer = clickConsumer;
    }

    public SlotImpl(Coords coords, InventoryLayer layer, ServiceInventory inventory, Item item) {
        this.coords = coords;
        this.layer = layer;
        this.inventory = inventory;;
        this.item = item;
    }

    @Override
    public Coords getCoords() {
        return coords;
    }

    @Override
    public InventoryLayer getLayer() {
        return layer;
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
        clickConsumer.accept(click);
    }

    @Override
    public void setClickConsumer(Consumer<Click> clickConsumer) {
        this.clickConsumer = clickConsumer;
    }

    @Override
    public void setLayer(InventoryLayer layer) {
        this.layer = layer;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public JSONObject serialize() {
        JSONObject json = new JSONObject();
        json.put("x", coords.getX());
        json.put("y", coords.getY());
        if (item != null && item instanceof ItemImpl) {
            json.put("item", ((ItemImpl) item).serialize());
        }
        return json;
    }

    public static SlotImpl deserialize(JSONObject json, InventoryLayer layer, ServiceInventory serviceInventory) {
        int x = json.getIntValue("x");
        int y = json.getIntValue("y");

        SlotImpl slot = new SlotImpl(x, y, layer, serviceInventory);

        if (json.containsKey("item") && json.getJSONObject("item") != null) {
            slot.setItem(ItemImpl.deserialize(json.getJSONObject("item")));
        }

        return slot;
    }
}