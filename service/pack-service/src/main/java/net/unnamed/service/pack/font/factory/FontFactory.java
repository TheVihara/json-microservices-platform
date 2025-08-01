package net.unnamed.service.pack.font.factory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.key.Key;
import net.unnamed.service.pack.api.BitMapFont;
import net.unnamed.service.pack.api.dao.BitMapFontDao;
import net.unnamed.service.pack.font.config.FontConfig;
import net.unnamed.service.pack.font.config.provider.*;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Vector2Float;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.font.UnihexFontProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FontFactory {
    BitMapFontDao bitMapFontDao;

    public Font createFont(String namespace, String value, List<FontProvider> providers) {
        return createFont(Key.key(namespace, value), providers);
    }

    public Font createFont(Key key, List<FontProvider> providers) {
        return Font.font(key)
                .providers(providers);
    }

    public void duplicateFont(ResourcePack resourcePack, Font font, FontConfig fontConfig) {
        Map<String, Font> duplicatedFonts = new HashMap<>();
        List<BitmapProviderConfig> bitmapFontProviders = fontConfig.getBitmapFontProviders();
        List<ReferenceProviderConfig> referenceFontProviders = fontConfig.getReferenceFontProviders();
        List<SpaceProviderConfig> spaceFontProviders = fontConfig.getSpaceProvider();
        List<TrueTypeProviderConfig> trueTypeProviderConfigs = fontConfig.getTrueTypeFontProviders();
        List<UnihexProviderConfig> unihexProviderConfigs = fontConfig.getUnihexFontProviders();

        if (bitmapFontProviders != null) {
            for (BitmapProviderConfig providerConfig : bitmapFontProviders) {
                if (providerConfig.getOffset() == null) {
                    continue;
                }

                for (Integer offset : providerConfig.getOffset()) {
                    String value = font.key().value() + "_offset_" + offset;
                    Font duplicatedFont = createFont(
                            font.key().namespace(),
                            value,
                            List.of(createBitMapFontProvider(
                                    providerConfig.getNamespace(),
                                    providerConfig.getValue(),
                                    providerConfig.getHeight(),
                                    providerConfig.getAscent() + offset,
                                    providerConfig.getCharacters()
                            ))
                    );

                    duplicatedFonts.put(value, duplicatedFont);
                }
            }
        }

        if (trueTypeProviderConfigs != null) {
            for (TrueTypeProviderConfig providerConfig : trueTypeProviderConfigs) {
                if (providerConfig.getOffset() == null) {
                    continue;
                }

                for (Double offset : providerConfig.getOffset()) {
                    String value = font.key().value() + "_offset_" + offset;
                    FontProvider fontProvider = createTrueTypeFontProvider(
                            providerConfig.getNamespace(),
                            providerConfig.getValue(),
                            providerConfig.getOversample(),
                            providerConfig.getShiftX(),
                            providerConfig.getShiftY() - offset.floatValue(),
                            providerConfig.getSize(),
                            providerConfig.getSkip()
                    );

                    if (duplicatedFonts.containsKey(value)) {
                        Font duplicatedFont = duplicatedFonts.get(value);
                        ArrayList<FontProvider> newProviders = new ArrayList<>(duplicatedFont.providers());
                        newProviders.add(fontProvider);
                        duplicatedFonts.put(value, duplicatedFont.providers(newProviders));
                    } else {
                        Font duplicatedFont = createFont(
                                font.key().namespace(),
                                value,
                                List.of(fontProvider)
                        );

                        duplicatedFonts.put(value, duplicatedFont);
                    }
                }
            }
        }

        for (Font duplicatedFont : duplicatedFonts.values()) {
            resourcePack.font(duplicatedFont);
        }
    }

    public List<FontProvider> createFontProviders(FontConfig fontConfig) {
        List<FontProvider> providers = new ArrayList<>();

        if (fontConfig.getBitmapFontProviders() != null) {
            for (BitmapProviderConfig providerConfig : fontConfig.getBitmapFontProviders()) {
                if (providerConfig.getKey() != null) {
                    bitMapFontDao.save(new BitMapFont(
                            providerConfig.getKey(),
                            providerConfig.getCharacters().get(0),
                            providerConfig.getWidth()
                    ));
                }
                providers.add(this.createBitMapFontProvider(
                        providerConfig.getNamespace(),
                        providerConfig.getValue(),
                        providerConfig.getHeight(),
                        providerConfig.getAscent(),
                        providerConfig.getCharacters()
                ));
            }
        }

        if (fontConfig.getReferenceFontProviders() != null) {
            for (ReferenceProviderConfig providerConfig : fontConfig.getReferenceFontProviders()) {
                providers.add(this.createReferenceFontProvider(providerConfig.getNamespace(), providerConfig.getValue()));
            }
        }

        if (fontConfig.getSpaceProvider() != null) {
            for (SpaceProviderConfig providerConfig : fontConfig.getSpaceProvider()) {
                providers.add(this.createSpaceFontProvider(providerConfig.getAdvances()));
            }
        }

        if (fontConfig.getTrueTypeFontProviders() != null) {
            for (TrueTypeProviderConfig providerConfig : fontConfig.getTrueTypeFontProviders()) {
                providers.add(this.createTrueTypeFontProvider(
                        providerConfig.getNamespace(),
                        providerConfig.getValue(),
                        providerConfig.getOversample(),
                        providerConfig.getShiftX(),
                        providerConfig.getShiftY(),
                        providerConfig.getSize(),
                        providerConfig.getSkip()
                ));
            }
        }

        if (fontConfig.getUnihexFontProviders() != null) {
            for (UnihexProviderConfig providerConfig : fontConfig.getUnihexFontProviders()) {
                providers.add(this.createUnihexFontProvider(providerConfig.getNamespace(), providerConfig.getValue(), List.of())); // TODO: impl size overrides
            }
        }

        return providers;
    }

    public FontProvider createBitMapFontProvider(String namespace, String value, int height, int ascent, List<String> characters) {
        return FontProvider.bitMap(
                Key.key(namespace, value),
                height,
                ascent,
                characters
        );
    }

    public FontProvider createSpaceFontProvider(Map<String, Integer> advances) {
        return FontProvider.space(advances);
    }

    public FontProvider createReferenceFontProvider(String namespace, String value) {
        return FontProvider.reference(Key.key(namespace, value));
    }

    public FontProvider createTrueTypeFontProvider(String namespace, String value, float oversample, float shiftX, float shiftY, float size, List<String> skip) {
        return FontProvider.trueType().file(Key.key(namespace, value))
                .oversample(oversample)
                .shift(new Vector2Float(shiftX, shiftY))
                .skip(skip)
                .size(size)
                .build();
    }

    public FontProvider createUnihexFontProvider(String namespace, String value, List<UnihexFontProvider.SizeOverride> sizeOverrides) {
        return FontProvider.unihex(Key.key(namespace, value), sizeOverrides);
    }
}
