package net.unnamed.minecraft.paper.essentials.teleport;

import net.unnamed.minecraft.paper.essentials.player.EssentialsPlayer;

public class TeleportRequest {
    private final EssentialsPlayer sender;
    private final EssentialsPlayer receiver;
    private final long sentAt;

    public TeleportRequest(EssentialsPlayer sender, EssentialsPlayer receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.sentAt = System.currentTimeMillis();
    }

    public EssentialsPlayer getSender() {
        return sender;
    }

    public EssentialsPlayer getReceiver() {
        return receiver;
    }

    public long getSentAt() {
        return sentAt;
    }
}
