package net.unnamed.service.pack;

import net.unnamed.service.pack.factory.PackFactory;
import net.unnamed.service.pack.factory.TextureFactory;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.texture.Texture;

import java.io.File;
import java.nio.file.Path;

public class PackManager {
    private final PackFactory packFactory = new PackFactory();
    private final TextureFactory textureFactory = new TextureFactory();
    private final Path dataFolder;

    public PackManager(Path dataFolder) {
        this.dataFolder = dataFolder;
        if (!dataFolder.toFile().exists()) {
            dataFolder.toFile().mkdirs();
        }
    }

    public ResourcePack generatePack() {
        ResourcePack resourcePack = packFactory.createPack();
        scanForTextures(resourcePack);

        return resourcePack;
    }

    public void scanForTextures(ResourcePack resourcePack) {
        Path textureFolder = dataFolder.resolve("textures");
        if (!textureFolder.toFile().exists()) {
            textureFolder.toFile().mkdirs();
            return;
        }
        File[] files = textureFolder.toFile().listFiles();
        if (files == null) {
            return;
        }
        scanForTextures(resourcePack, textureFolder, files);
    }

    private void scanForTextures(ResourcePack resourcePack, Path textureFolder, File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                File[] subFiles = file.listFiles();
                if (subFiles == null) {
                    continue;
                }
                scanForTextures(resourcePack, textureFolder, subFiles);
            } else {
                Path filePath = file.toPath();
                Path relativePath = textureFolder.relativize(filePath);
                String texturePath = relativePath.toString().replace(File.separatorChar, '/');
                Texture foundTexture = textureFactory.createTexture("astopia", texturePath, file.getAbsolutePath());
                resourcePack.texture(foundTexture);
            }
        }
    }
}
