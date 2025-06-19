package net.unnamed.service.player.api;

import net.kyori.adventure.text.Component;
import net.unnamed.service.command.api.CommandSender;

import java.net.SocketAddress;
import java.util.UUID;

public interface PlayerBase extends CommandSender {
    UUID getUuid();
    String getName();
    SocketAddress getSocketAddress();

    void sendMessage(String message);
    void sendMessage(Component message);

    @Override
    default boolean isConsole() {
        return false;
    }
}
