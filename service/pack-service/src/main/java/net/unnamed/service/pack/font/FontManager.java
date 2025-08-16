package net.unnamed.service.pack.font;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.unnamed.common.config.YamlConfig;
import net.unnamed.service.pack.api.NegativeFontRegistry;
import net.unnamed.service.pack.api.dao.BitMapFontDao;
import net.unnamed.service.pack.font.config.FontConfig;
import net.unnamed.service.pack.font.factory.FontFactory;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.font.FontProvider;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FontManager {
    static Logger logger = Logger.getLogger("FontManager");
    static NegativeFontRegistry negativeFontRegistry = new NegativeFontRegistry();

    BitMapFontDao bitMapFontDao;
    FontFactory fontFactory;

    public FontManager(HikariDataSource dataSource) {
        this.bitMapFontDao = new BitMapFontDao(dataSource);
        this.fontFactory = new FontFactory(bitMapFontDao);

        this.bitMapFontDao.init().join();
    }

    public void scan(ResourcePack resourcePack, Path dataFolder) {
        generateNegativeFonts(resourcePack);

        Path fontFolder = dataFolder.resolve("font");
        if (!fontFolder.toFile().exists()) {
            fontFolder.toFile().mkdirs();
            return;
        }
        File[] files = fontFolder.toFile().listFiles();
        if (files == null) {
            return;
        }
        scan(resourcePack, fontFolder, files);
    }

    private void generateNegativeFonts(ResourcePack resourcePack) {
        for (String fontName : negativeFontRegistry.getRegisteredFontNames()) {
            NegativeFontRegistry.FontData fontData = negativeFontRegistry.getRegisteredFonts().get(fontName);
            int[] advances = fontData.getAdvances();
            String imageFile = fontData.getImageFile();

            generateBitmapFont(resourcePack, fontName, advances, imageFile);
        }
    }

    private void generateBitmapFont(ResourcePack resourcePack, String fontName, int[] advances, String imageFile) {
        char baseChar = negativeFontRegistry.getBaseCharacter(fontName);
        List<FontProvider> providers = new ArrayList<>();
        providers.add(fontFactory.createSpaceFontProvider(Map.of(" ", 4)));

        for (int i = 0; i < advances.length; i++) {
            providers.add(fontFactory.createBitMapFontProvider(
                    "astopia",
                    imageFile,
                    advances[i],
                    -32768,
                    List.of(String.valueOf((char) (baseChar + i)))
            ));
        }

        resourcePack.font(fontFactory.createFont("astopia", fontName, providers));
    }


    private void scan(ResourcePack resourcePack, Path fontsFolder, File[] files) {
        for (File file : files) {
            if (!file.getName().endsWith(".yml")) {
                Path filePath = file.toPath();
                Path relativePath = fontsFolder.relativize(filePath);
                String fontPath = relativePath.toString().replace(File.separatorChar, '/');
                resourcePack.unknownFile("assets/astopia/font/" + fontPath, Writable.file(filePath.toFile()));
                continue;
            }
            if (file.isDirectory()) {
                File[] subFiles = file.listFiles();
                if (subFiles == null) {
                    continue;
                }
                scan(resourcePack, fontsFolder, subFiles);
            } else {
                Path filePath = file.toPath();
                Path relativePath = fontsFolder.relativize(filePath);
                String fontPath = relativePath.toString().replace(File.separatorChar, '/').replaceAll(".yml", "");
                FontConfig fontConfig = YamlConfig.loadSafe(FontConfig.class, filePath, FontConfig::new);
                Font foundFont = fontFactory.createFont(
                        "astopia",
                        fontPath,
                        fontFactory.createFontProviders(fontConfig)
                );
                resourcePack.font(foundFont);
                fontFactory.duplicateFont(resourcePack, foundFont, fontConfig);
            }
        }
    }
}
