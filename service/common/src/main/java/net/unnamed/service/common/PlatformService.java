package net.unnamed.service.common;

import net.unnamed.common.logging.ConsoleLogger;
import net.unnamed.common.logging.PlatformLogger;
import net.unnamed.common.nats.NatsManager;
import net.unnamed.common.packet.PacketRegistry;

public abstract class PlatformService implements Service {
    protected final PlatformLogger logger;
    protected final PacketRegistry packetRegistry;
    private final String name;
    private final String description;
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
        NatsManager.INSTANCE.init();
        onLoad();
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

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
