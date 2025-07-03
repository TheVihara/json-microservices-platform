package net.unnamed.service.gui.api;

import net.unnamed.service.player.api.PlayerBase;

public interface InventoryViewer {
    PlayerBase getPlayer();
    long getOpenedAtMillis();
}
