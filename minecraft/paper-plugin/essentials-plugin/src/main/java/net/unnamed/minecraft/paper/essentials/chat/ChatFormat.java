package net.unnamed.minecraft.paper.essentials.chat;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

@FunctionalInterface
public interface ChatFormat {
    Component format(Player player, Component message);
}
