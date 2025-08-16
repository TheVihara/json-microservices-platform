package net.unnamed.service.pack.texture;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.unnamed.common.config.YamlConfig;
import net.unnamed.service.pack.texture.factory.TextureFactory;
import net.unnamed.service.pack.texture.meta.MetaFactory;
import net.unnamed.service.pack.texture.meta.config.MetadataConfig;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.texture.Texture;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TextureManager {
    TextureFactory textureFactory = new TextureFactory();
    MetaFactory metaFactory = new MetaFactory();

    public void scan(ResourcePack resourcePack, Path dataFolder) {
        Path textureFolder = dataFolder.resolve("textures");
        if (!textureFolder.toFile().exists()) {
            textureFolder.toFile().mkdirs();
            return;
        }
        File[] files = textureFolder.toFile().listFiles();
        if (files == null) {
            return;
        }
        scan(resourcePack, dataFolder.toFile().getName(), textureFolder, files);
    }

    private void scan(ResourcePack resourcePack, String namespace, Path textureFolder, File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                File[] subFiles = file.listFiles();
                if (subFiles == null) {
                    continue;
                }
                scan(resourcePack, namespace, textureFolder, subFiles);
            } else if (file.getName().endsWith(".png")) {
                Path filePath = file.toPath();
                Path relativePath = textureFolder.relativize(filePath);
                String texturePath = relativePath.toString().replace(File.separatorChar, '/');
                Texture foundTexture = textureFactory.createTexture(namespace, texturePath, file.getAbsolutePath());

                Optional<File> optionalMetaFile = Arrays.stream(files).filter(f -> f.getName().equalsIgnoreCase(file.getName() + ".mcmeta.yml"))
                        .findFirst();

                if (optionalMetaFile.isPresent()) {
                    File metaFile = optionalMetaFile.get();
                    MetadataConfig metadataConfig = YamlConfig.loadSafe(MetadataConfig.class, metaFile.toPath(), MetadataConfig::new);
                    foundTexture = foundTexture.meta(metaFactory.createMetadata(metadataConfig));
                }

                resourcePack.texture(foundTexture);
            }
        }
    }
}
