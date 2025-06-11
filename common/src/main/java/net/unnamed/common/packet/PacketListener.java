package net.unnamed.common.packet;

import com.google.protobuf.MessageLite;

public interface PacketListener<P extends MessageLite> {
    void handle(P message);
}
