package net.unnamed.minecraft.paper.module.container.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.unnamed.service.pack.api.NegativeFontRegistry;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PaddingExpansion extends PlaceholderExpansion {
    NegativeFontRegistry negativeFontRegistry = new NegativeFontRegistry();

    @Override
    public @NotNull String getIdentifier() {
        return "padding";
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

    public String onPlaceholderRequest(Player player, @NotNull String params) {
        String[] parts = params.split("_");
        if (parts.length < 3) {
            return null;
        }
        try {
            boolean isSplit = false;
            int offset = 0;
            if (parts[0].equals("split")) {
                isSplit = true;
                offset = 1;
            }
            String type = parts[offset];
            String direction = parts[offset + 1];
            int value = Integer.parseInt(parts[offset + 2]);
            if (direction.equals("neg")) {
                value = -value;
            }
            return switch (type) {
                case "full" -> negativeFontRegistry.createPaddingString(value, isSplit);
                case "char" -> {
                    Character ch = negativeFontRegistry.getClosestPaddingCharacter(value, isSplit);
                    yield ch != null ? String.valueOf(ch) : "";
                }
                case "complex" -> negativeFontRegistry.createComplexPadding(value, isSplit);
                case "font" -> {
                    String fontName = value < 0 ?
                            (isSplit ? "negative_padding_split" : "negative_padding_nosplit") :
                            (isSplit ? "padding_split" : "padding_nosplit");
                    NegativeFontRegistry.FontData fontData = negativeFontRegistry.getRegisteredFonts().get(fontName);
                    yield fontData != null ? "<font:" + fontData.getFullFontName() + ">" : "";
                }
                default -> null;
            };
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

}
