package net.unnamed.service.gui.api.serializer;

import com.alibaba.fastjson2.JSONObject;
import net.unnamed.service.gui.api.Gui;
import net.unnamed.service.gui.api.inventory.ServiceInventory;

public enum SerializerType {
    INVENTORY(new ServiceInventory.Serializer(), ServiceInventory.class);

    private final Gui.Serializable<?> serializable;
    private final Class<? extends Gui> type;

    SerializerType(Gui.Serializable<?> serializable, Class<? extends Gui> type) {
        this.serializable = serializable;
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public <T extends Gui> Gui.Serializable<T> getSerializer() {
        return (Gui.Serializable<T>) serializable;
    }

    /**
     * Non-static: Serialize a GUI instance known to match this registry's type.
     */
    @SuppressWarnings("unchecked")
    public JSONObject serializeTyped(Gui gui) {
        if (!type.isInstance(gui)) {
            throw new IllegalArgumentException("Invalid GUI type: expected " + type + ", got " + gui.getClass());
        }
        JSONObject json = ((Gui.Serializable<Gui>) serializable).serialize(gui);
        json.put("serializer", name());
        return json;
    }

    /**
     * Static: Automatically find and serialize a GUI instance.
     */
    @SuppressWarnings("unchecked")
    public static JSONObject serialize(Gui gui) {
        for (SerializerType registry : values()) {
            if (registry.type.isInstance(gui)) {
                return registry.serializeTyped(gui);
            }
        }
        throw new IllegalArgumentException("No serializer registered for GUI type: " + gui.getClass());
    }

    /**
     * Static: Deserialize JSON by finding the correct serializer.
     */
    public static Gui deserialize(JSONObject jsonObject) {
        String serializerName = jsonObject.getString("serializer");
        if (serializerName == null) {
            throw new IllegalArgumentException("Missing serializer in JSON");
        }

        SerializerType registry;
        try {
            registry = SerializerType.valueOf(serializerName);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown serializer: " + serializerName, e);
        }

        return registry.getSerializer().deserialize(jsonObject);
    }
}
