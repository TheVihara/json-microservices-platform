package net.unnamed.common.packet;

@FunctionalInterface
public interface PacketListener<T extends Packet> {
    void onPacket(T packet);
}