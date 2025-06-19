package net.unnamed.common.packet;

import net.unnamed.common.nats.NatsManager;
import net.unnamed.common.packet.impl.PingPacket;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class PacketRegistry {
    private final ConcurrentHashMap<String, Class<? extends Packet>> packets = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Class<? extends Packet>, List<PacketListener<? extends Packet>>> listeners = new ConcurrentHashMap<>();

    public PacketRegistry() {
        registerPacket(PingPacket.ID, PingPacket.class);
    }

    public void subscribe(String subject) {
        NatsManager.INSTANCE.subscribe(subject, new PacketHandler(this));
    }

    public void registerPacket(String id, Class<? extends Packet> packet) {
        packets.put(id, packet);
    }

    public <T extends Packet> void registerListener(Class<T> packetClass, PacketListener<T> listener) {
        listeners.computeIfAbsent(packetClass, k -> new CopyOnWriteArrayList<>()).add(listener);
    }

    public Class<? extends Packet> getPacket(String id) {
        return packets.get(id);
    }

    public List<PacketListener<? extends Packet>> getListeners(Class<? extends Packet> packetClass) {
        return listeners.getOrDefault(packetClass, List.of());
    }
}
