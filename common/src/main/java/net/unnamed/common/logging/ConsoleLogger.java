package net.unnamed.common.logging;

import java.util.logging.*;

public final class ConsoleLogger extends PlatformLogger {

    public ConsoleLogger() {
        super("net.unnamed.common");

        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");

        this.setLevel(Level.ALL);
        this.setUseParentHandlers(false);

        ConsoleFormatter fmt = new ConsoleFormatter();
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        handler.setFormatter(fmt);

        this.addHandler(handler);
    }
}