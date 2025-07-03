package net.unnamed.service.gui.api.action;

import net.unnamed.service.player.api.PlayerBase;

public interface Click {
    PlayerBase getPlayer();
    ClickType getType();
}
