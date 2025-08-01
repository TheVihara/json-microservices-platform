package net.unnamed.service.gui.api;

import com.alibaba.fastjson2.JSONObject;
import net.unnamed.service.gui.api.handler.SlotHandler;
import net.unnamed.service.gui.api.slot.Slot;

import java.util.List;

public interface Gui {
    List<InventoryViewer> getViewers();
    SlotHandler getSlotHandler();
    Serializable<?> getSerializable();
    void addViewer(InventoryViewer viewer);
    void removeViewer(InventoryViewer viewer);
    void open(InventoryViewer viewer);
    void close(InventoryViewer viewer);
    void draw(InventoryViewer viewer);
    void draw(Slot slot);

    interface Serializable<T extends Gui> {
        JSONObject serialize(T gui);
        T deserialize(JSONObject jsonObject);
    }
}
