package net.unnamed.common.packet;

import com.alibaba.fastjson2.annotation.JSONField;
import io.nats.client.Message;

public abstract class Packet {
    private String id;

    @JSONField(serialize = false, deserialize = false)
    private transient Message originalMessage;

    public Packet() {

    }

    public Packet(String id) {
        this.id = id;
    }

    public Packet(String id, Message originalMessage) {
        this.id = id;
        this.originalMessage = originalMessage;
    }

    public void setOriginalMessage(Message originalMessage) {
        this.originalMessage = originalMessage;
    }

    public String getId() {
        return id;
    }

    public Message getOriginalMessage() {
        return originalMessage;
    }
}
