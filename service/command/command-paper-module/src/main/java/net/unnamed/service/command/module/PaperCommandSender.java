package net.unnamed.service.command.module;

import net.unnamed.service.command.api.CommandSender;
import org.bukkit.entity.Player;

public class PaperCommandSender implements CommandSender {
    private final org.bukkit.command.CommandSender bukkitSender;

    public PaperCommandSender(org.bukkit.command.CommandSender bukkitSender) {
        this.bukkitSender = bukkitSender;
    }

    @Override
    public String getName() {
        return bukkitSender.getName();
    }

    @Override
    public boolean isConsole() {
        return !(bukkitSender instanceof Player);
    }

    @Override
    public void sendMessage(String message) {
        bukkitSender.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return bukkitSender.hasPermission(permission);
    }

    public org.bukkit.command.CommandSender getBukkitSender() {
        return bukkitSender;
    }
}