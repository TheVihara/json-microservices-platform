package net.unnamed.service.pack;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.key.Key;
import net.unnamed.service.pack.factory.PackFactory;
import net.unnamed.service.pack.font.FontManager;
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
    Path dataFolder;

    public PackManager(Path dataFolder) {
        this.dataFolder = dataFolder;

        if (!dataFolder.toFile().exists()) {
            dataFolder.toFile().mkdirs();
        }

        this.textureManager = new TextureManager();
        this.fontManager = new FontManager();
        this.modelManager = new ModelManager(dataFolder);
    }

    public ResourcePack generatePack() {
        ResourcePack resourcePack = packFactory.createPack();
        File packFolder = dataFolder.toFile();

        if (packFolder.listFiles() == null) {
            return null;
        }

        for (File file : packFolder.listFiles()) {
            if (!file.isDirectory()) {
                continue;
            }
            textureManager.scan(resourcePack, file.toPath());
            fontManager.scan(resourcePack, file.toPath());
        }

        modelManager.scan(resourcePack);

        HashMap<String, String> lang = new HashMap<>();
        lang.put("container.inventory", "");
        Language language = Language.language(Key.key("minecraft", "en_us"), lang);
        resourcePack.language(language);

        return resourcePack;
    }
}
