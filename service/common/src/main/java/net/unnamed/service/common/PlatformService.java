package net.unnamed.service.common;

import net.unnamed.common.logging.ConsoleLogger;
import net.unnamed.common.logging.PlatformLogger;
import net.unnamed.common.nats.NatsManager;
import net.unnamed.common.packet.PacketRegistry;

public abstract class PlatformService implements Service {
    protected final PlatformLogger logger;
    protected final PacketRegistry packetRegistry;
    private String name;
    private String description;
    private boolean enabled = true;

    public PlatformService(String name, String description) {
        this.logger = new ConsoleLogger();
        this.packetRegistry = new PacketRegistry();
        this.name = name;
        this.description = description;
    }

    public PlatformService() {
        this.logger = new ConsoleLogger();
        this.packetRegistry = new PacketRegistry();
        this.name = null;
        this.description = null;
    }

    public final void load() {
        logger.info("Loading " + name + " service...");
        long time = System.currentTimeMillis();
        NatsManager.INSTANCE.init();
        onLoad();
        time = System.currentTimeMillis() - time;
        logger.info("Loaded {} service in {}ms", name, time);
    }

    public void onInput(String input) {

    }

    @Override
    public PacketRegistry getPacketRegistry() {
        return packetRegistry;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public PlatformLogger getLogger() {
        return logger;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
