package net.unnamed.common.packet;

import com.google.protobuf.MessageLite;

public interface Packet extends MessageLite {
    String getId();
}
