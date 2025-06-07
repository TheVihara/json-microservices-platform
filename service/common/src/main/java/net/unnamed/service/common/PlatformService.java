package net.unnamed.service.common;

import java.util.logging.Logger;

public abstract class PlatformService implements Service {
    protected final Logger logger;
    private final String name;
    private final String description;
    private boolean enabled = true;

    public PlatformService(String name, String description) {
        this.logger = Logger.getLogger(name);
        this.name = name;
        this.description = description;
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
