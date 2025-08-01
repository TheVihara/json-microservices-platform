package net.unnamed.service.pack;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.key.Key;
import net.unnamed.service.pack.atlas.AtlasManager;
import net.unnamed.service.pack.factory.PackFactory;
import net.unnamed.service.pack.font.FontManager;
import net.unnamed.service.pack.item.ItemManager;
import net.unnamed.service.pack.model.ModelManager;
import net.unnamed.service.pack.texture.TextureManager;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.lang.Language;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PackManager {
    PackFactory packFactory = new PackFactory();
    TextureManager textureManager;
    FontManager fontManager;
    ModelManager modelManager;
    ItemManager itemManager;
    AtlasManager atlasManager;
    Path dataFolder;

    public PackManager(Path dataFolder, HikariDataSource dataSource) {
        this.dataFolder = dataFolder;

        if (!dataFolder.toFile().exists()) {
            dataFolder.toFile().mkdirs();
        }

        this.textureManager = new TextureManager();
        this.fontManager = new FontManager(dataSource);
        this.modelManager = new ModelManager();
        this.itemManager = new ItemManager();
        this.atlasManager = new AtlasManager();
    }

    public ResourcePack generatePack() {
        ResourcePack resourcePack = packFactory.createPack();
        File packFolder = dataFolder.toFile();
        File[] files = packFolder.listFiles();

        if (files == null) {
            return null;
        }

        for (File file : files) {
            if (!file.isDirectory()) {
                continue;
            }

            Path path = file.toPath();
            textureManager.scan(resourcePack, path);
            fontManager.scan(resourcePack, path);
            modelManager.scan(resourcePack, path);
            itemManager.scan(resourcePack, path);
        }

        atlasManager.scan(resourcePack, packFolder.toPath());

        HashMap<String, String> lang = new HashMap<>();
        lang.put("container.inventory", "");
        Language language = Language.language(Key.key("minecraft", "en_us"), lang);
        resourcePack.language(language);

        return resourcePack;
    }
}
