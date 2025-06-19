package net.unnamed.service.command.api;

import java.util.logging.Logger;

public class ConsoleCommandSender implements CommandSender {
    private final Logger logger;

    public ConsoleCommandSender(Logger logger) {
        this.logger = logger;
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public boolean isConsole() {
        return true;
    }

    @Override
    public void sendMessage(String message) {
        logger.info(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }
}
