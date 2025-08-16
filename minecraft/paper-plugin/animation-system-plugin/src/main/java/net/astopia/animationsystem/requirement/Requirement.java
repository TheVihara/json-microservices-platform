package net.astopia.animationsystem.requirement;

import org.bukkit.entity.Player;

public interface Requirement {
    boolean isMet(Player player);
}
