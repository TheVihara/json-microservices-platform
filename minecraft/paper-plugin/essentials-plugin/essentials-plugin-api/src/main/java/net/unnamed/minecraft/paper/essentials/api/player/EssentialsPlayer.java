package net.unnamed.minecraft.paper.essentials.api.player;

import lombok.*;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import net.unnamed.service.player.api.PlayerBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.net.SocketAddress;
import java.sql.Timestamp;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
public class EssentialsPlayer implements PlayerBase {
    @NonNull
    Integer id;

    @NonNull
    UUID uuid;

    @NonNull
    String name;

    Timestamp lastLogin;

    double balance;

    SocketAddress socketAddress;

    public EssentialsPlayer(@NonNull Integer id, @NonNull UUID uuid, @NonNull String name, Timestamp lastLogin, double balance, SocketAddress socketAddress) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.lastLogin = lastLogin;
        this.balance = balance;
        this.socketAddress = socketAddress;
    }

    public EssentialsPlayer(Player player) {
        this.id = -1;
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.lastLogin = new Timestamp(System.currentTimeMillis());
        this.balance = 0.0;
        this.socketAddress = player.getAddress();
    }

    @Override
    public Object getHandle() {
        return Bukkit.getPlayer(uuid);
    }

    @Override
    public void sendMessage(String message) {
        Player player = Bukkit.getPlayer(this.uuid);
        if (player != null) player.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        Player player = Bukkit.getPlayer(this.uuid);
        return player != null && player.hasPermission(permission);
    }

    @Override
    public void sendMessage(Component message) {
        Player player = Bukkit.getPlayer(this.uuid);
        if (player != null) player.sendMessage(message);
    }

    public boolean isOnline() {
        Player player = Bukkit.getPlayer(this.uuid);
        return player != null && player.isOnline();
    }
}