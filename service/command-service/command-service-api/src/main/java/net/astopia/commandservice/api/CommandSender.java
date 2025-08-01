package net.astopia.commandservice.api;

public interface CommandSender {
    boolean hasPermission(String permission);
    boolean isConsole();
}
