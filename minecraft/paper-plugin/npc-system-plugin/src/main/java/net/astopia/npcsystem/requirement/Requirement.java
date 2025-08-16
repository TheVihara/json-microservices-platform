package net.astopia.npcsystem.requirement;

import org.bukkit.entity.Player;

public interface Requirement {
    boolean meetsCriteria(Player player);
}
