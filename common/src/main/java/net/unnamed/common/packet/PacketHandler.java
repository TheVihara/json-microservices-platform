package net.unnamed.common.packet;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.JSONReader;
import io.nats.client.Message;
import io.nats.client.MessageHandler;

import java.util.List;

public class PacketHandler implements MessageHandler {
    private final PacketRegistry packetRegistry;

    public PacketHandler(PacketRegistry packetRegistry) {
        this.packetRegistry = packetRegistry;
    }

    private static final JSONPath ID_PATH = JSONPath.of("$.id");

    @Override
    public void onMessage(Message message) {
        byte[] data = message.getData();
        String id = ID_PATH.extract(JSONReader.of(data)).toString();
        Class<? extends Packet> packetClass = packetRegistry.getPacket(id);
        JSONObject jsonObject = JSON.parseObject(data);

        System.out.println(jsonObject);

        Packet packet = JSON.parseObject(data, packetClass);
        List<PacketListener<? extends Packet>> packetListeners = packetRegistry.getListeners(packetClass);

        packet.setOriginalMessage(message);

        for (PacketListener<? extends Packet> listener : packetListeners) {
            @SuppressWarnings("unchecked")
            PacketListener<Packet> typedListener = (PacketListener<Packet>) listener;
            typedListener.onPacket(packet);
        }
    }
}
