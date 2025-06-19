package net.unnamed.common.packet.impl;

import net.unnamed.common.packet.Packet;

public class PingPacket extends Packet {
    public static final String ID = "ping";

    public PingPacket() {
        super(ID);
    }
}
