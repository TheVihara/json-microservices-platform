package net.unnamed.service.gui.api.packet;

import com.alibaba.fastjson2.JSON;
import net.unnamed.service.gui.api.Gui;
import net.unnamed.service.gui.api.inventory.ChestServiceInventory;
import net.unnamed.service.gui.api.item.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles serialization/deserialization of GUI objects for packet transmission
 */
public class GuiSerializer {

    public String serializeGui(Gui gui) {
        GuiData data = new GuiData();
        data.type = gui.getClass().getSimpleName();
        data.title = "GUI"; // You can add a getTitle method to your GUI interface
        data.size = 27; // Default size

        if (gui instanceof ChestServiceInventory) {
            ChestServiceInventory chestGui = (ChestServiceInventory) gui;
            data.size = chestGui.getSize();
        }

        return JSON.toJSONString(data);
    }

    public String serializeItem(Item item) {
        if (item == null) return null;

        ItemData data = new ItemData();
        data.material = item.getMaterial();
        data.amount = item.getAmount();
        data.name = item.getName() != null ? item.getName().toString() : null;
        data.customModelData = item.getCustomModelData();
        // Convert lore to string list if needed

        return JSON.toJSONString(data);
    }

    // These would be implemented in actual service with factory pattern
    public Gui deserializeGui(String guiType, String guiData) {
        return null; // Placeholder
    }

    public Item deserializeItem(String itemData) {
        return null; // Placeholder
    }

    // Data classes for serialization
    public static class GuiData {
        public String type;
        public int size;
        public String title;
        public Map<String, Object> properties = new HashMap<>();
    }

    public static class ItemData {
        public String material;
        public int amount = 1;
        public String name;
        public int customModelData;
        public String[] lore;
    }
}
