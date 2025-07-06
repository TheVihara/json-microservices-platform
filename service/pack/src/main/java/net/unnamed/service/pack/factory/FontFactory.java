package net.unnamed.service.pack.factory;

import net.kyori.adventure.key.Key;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.font.FontProvider;

import java.util.List;

public class FontFactory {
    public Font createFont(String namespace, String value) {
        return Font.font(Key.key(namespace, value))
                .providers(List.of());
    }

    public FontProvider createBitMapFontProvider(String namespace, String value, int height, int ascent, List<String> characters) {
        return FontProvider.bitMap(
                Key.key(namespace, value),
                height,
                ascent,
                characters
        );
    }
}
