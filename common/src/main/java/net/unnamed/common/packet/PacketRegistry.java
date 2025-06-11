package net.unnamed.common.packet;

import com.google.protobuf.MessageLite;

import java.util.HashMap;

public class PacketRegistry {
    private final HashMap<Class<? extends MessageLite>, PacketListener<? extends MessageLite>> listeners = new HashMap<>();

    public <T extends MessageLite> void registerListener(Class<T> clazz, PacketListener<T> listener) {
        listeners.put(clazz, listener);
    }
}
