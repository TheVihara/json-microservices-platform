package net.unnamed.minecraft.paper.essentials.papi;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.unnamed.minecraft.paper.essentials.api.player.EssentialsPlayer;
import net.unnamed.minecraft.paper.essentials.player.PlayerManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EssentialsExpansion extends PlaceholderExpansion {
    PlayerManager playerManager;

    @Override
    public @NotNull String getIdentifier() {
        return "essentials";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Astopia";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return "";

        Optional<EssentialsPlayer> optional = playerManager.getCache().getByUUID(player.getUniqueId());
        if (optional.isEmpty()) return "";

        EssentialsPlayer data = optional.get();

        return switch (params.toLowerCase()) {
            case "name" -> data.getName();
            case "balance" -> String.format("%.2f", data.getBalance());
            case "lastlogin" -> {
                if (data.getLastLogin() == null) yield "Unknown";
                yield new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(data.getLastLogin());
            }
            default -> null;
        };
    }
}
