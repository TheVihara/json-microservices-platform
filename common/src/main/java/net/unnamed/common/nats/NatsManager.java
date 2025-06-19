package net.unnamed.common.nats;

import com.alibaba.fastjson2.JSON;
import io.nats.client.*;

import net.unnamed.common.packet.Packet;
import net.unnamed.common.packet.PacketHandler;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.*;

public enum NatsManager {
    INSTANCE;

    private Connection connection;
    private final Map<String, Dispatcher> dispatchers = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void init() {
        try {
            this.connection = Nats.connect();
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to NATS", e);
        }
    }

    /**
     * Subscribe to a subject with a PacketHandler.
     */
    public void subscribe(String subject, PacketHandler handler) {
        Dispatcher dispatcher = connection.createDispatcher(handler);
        dispatcher.subscribe(subject);
        dispatchers.put(subject, dispatcher);
    }

    /**
     * Publish a packet to a specific subject.
     */
    public void publish(String subject, Packet packet) {
        byte[] data = JSON.toJSONBytes(packet);
        connection.publish(subject, data);
    }

    /**
     * Request a response from a subject, fully asynchronous.
     */
    public <T> CompletableFuture<T> request(String subject, Packet packet, Class<T> responseType, Duration timeout) {
        String inbox = connection.createInbox();
        CompletableFuture<T> future = new CompletableFuture<>();

        Dispatcher dispatcher = connection.createDispatcher(msg -> {
            try {
                T response = JSON.parseObject(msg.getData(), responseType);
                future.complete(response);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });

        dispatcher.subscribe(inbox);

        byte[] data = JSON.toJSONBytes(packet);
        connection.publish(subject, inbox, data);

        scheduler.schedule(() -> {
            if (!future.isDone()) {
                future.completeExceptionally(new TimeoutException("Request timed out after " + timeout.toMillis() + "ms"));
                dispatcher.unsubscribe(inbox);
            }
        }, timeout.toMillis(), TimeUnit.MILLISECONDS);

        future.whenComplete((response, throwable) -> dispatcher.unsubscribe(inbox));

        return future;
    }

    /**
     * Respond to a message directly.
     */
    public void respond(Message message, Packet responsePacket) {
        if (message.getReplyTo() != null) {
            byte[] responseData = JSON.toJSONBytes(responsePacket);
            connection.publish(message.getReplyTo(), responseData);
        }
    }

    /**
     * Shutdown NATS connection and scheduler.
     */
    public void shutdown() {
        if (connection != null) {
            try {
                connection.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }
}
