package net.unnamed.common.nats;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import net.unnamed.common.packet.PacketHandler;

public enum NatsManager {
    INSTANCE;

    private Connection connection;
    private Dispatcher dispatcher;

    public void init() {
        try (Connection connection = Nats.connect()) {
            this.connection = connection;
            this.dispatcher = connection.createDispatcher(new PacketHandler());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
