package net.astopia.guipaperplugin.api.inventory;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import net.unnamed.service.gui.api.InventoryViewer;
import net.unnamed.service.player.api.PlayerBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.net.SocketAddress;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Getter
public class PlayerInventoryViewer implements PlayerBase<Player>, InventoryViewer {
    UUID uuid;
    String name;
    SocketAddress socketAddress;
    long openedAtMillis;

    public static PlayerInventoryViewer of(Player player) {
        return new PlayerInventoryViewer(
                player.getUniqueId(),
                player.getName(),
                player.getAddress(),
                System.currentTimeMillis()
        );
    }

    @Override
    public PlayerBase<Player> getPlayer() {
        return this;
    }

    @Override
    public long getOpenedAtMillis() {
        return openedAtMillis;
    }

    @Override
    public Player getHandle() {
        return Bukkit.getPlayer(uuid);
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SocketAddress getSocketAddress() {
        return socketAddress;
    }

    @Override
    public void sendMessage(String message) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            player.sendMessage(message);
        }
    }

    @Override
    public boolean hasPermission(String permission) {
        Player player = Bukkit.getPlayer(uuid);

        if (player != null) {
            return player.hasPermission(permission);
        }

        return false;
    }

    @Override
    public void sendMessage(Component message) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            player.sendMessage(message);
        }
    }
}
