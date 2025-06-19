package net.unnamed.service.command.api;

public interface CommandSender {
    String getName();
    boolean isConsole();
    void sendMessage(String message);
    boolean hasPermission(String permission);

    default void sendError(String message) {
        sendMessage("§c" + message);
    }

    default void sendSuccess(String message) {
        sendMessage("§a" + message);
    }

    default void sendInfo(String message) {
        sendMessage("§b" + message);
    }
}