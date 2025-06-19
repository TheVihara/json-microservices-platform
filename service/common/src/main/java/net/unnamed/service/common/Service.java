package net.unnamed.service.common;

import net.unnamed.common.ClassInstance;
import net.unnamed.common.packet.PacketRegistry;

public interface Service extends ClassInstance {
    PacketRegistry getPacketRegistry();
    String getName();
    String getDescription();
    boolean isEnabled();
    void onLoad();
}
