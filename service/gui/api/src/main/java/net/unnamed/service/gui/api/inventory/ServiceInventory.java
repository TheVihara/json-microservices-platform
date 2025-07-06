package net.unnamed.service.gui.api.inventory;

import com.alibaba.fastjson2.JSONObject;
import net.unnamed.service.gui.api.Gui;
import net.unnamed.service.gui.api.factory.InventoryFactory;
import net.unnamed.service.gui.api.serializer.SerializerType;

public interface ServiceInventory extends Gui {
    String getTitle();
    ServiceInventoryType getType();
    int getRows();
    int getColumns();

    default Gui.Serializable<ServiceInventory> getSerializer() {
        return SerializerType.INVENTORY.getSerializer();
    }

    class Serializer implements Gui.Serializable<ServiceInventory> {
        @Override
        public JSONObject serialize(ServiceInventory gui) {
            JSONObject jsonObject = JSONObject.of();
            jsonObject.put("title", gui.getTitle());
            jsonObject.put("type", gui.getType().name());
            jsonObject.put("rows", gui.getRows());
            jsonObject.put("columns", gui.getColumns());
            return jsonObject;
        }

        @Override
        public ServiceInventory deserialize(JSONObject jsonObject) {
            return InventoryFactory.getInstance().createInventory(
                    ServiceInventoryType.valueOf(jsonObject.getString("type")),
                    jsonObject.getString("title"),
                    jsonObject.getInteger("rows"),
                    jsonObject.getInteger("columns")
            );
        }
    }
}
