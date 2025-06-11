package net.unnamed.service.common;

import net.unnamed.common.packet.PacketRegistry;

import java.util.logging.Logger;

public abstract class PlatformService implements Service {
    protected final Logger logger;
    protected final PacketRegistry packetRegistry;
    private final String name;
    private final String description;
    private boolean enabled = true;

    public PlatformService(String name, String description) {
        this.logger = Logger.getLogger(name);
        this.packetRegistry = new PacketRegistry();
        this.name = name;
        this.description = description;
    }

    public PlatformService() {
        this.logger = Logger.getLogger(this.getClass().getName());
        this.packetRegistry = new PacketRegistry();
        this.name = null;
        this.description = null;
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
    public Logger getLogger() {
        return logger;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
