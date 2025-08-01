package net.unnamed.service.pack.atlas;

import de.bsommerfeld.jshepherd.core.ConfigurationLoader;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.key.Key;
import net.unnamed.service.pack.atlas.config.AtlasConfig;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.atlas.Atlas;
import team.unnamed.creative.atlas.AtlasSource;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AtlasManager {
    static Logger logger = Logger.getLogger("AtlasManager");

    public void scan(ResourcePack resourcePack, Path packFolder) {
        AtlasConfig atlasConfig = ConfigurationLoader.load(packFolder.resolve("atlas.yml"), AtlasConfig::new);
        List<String> textures = atlasConfig.getTextures();

        List<AtlasSource> atlasSources = new ArrayList<>();
        resourcePack.textures().forEach(texture -> {
            Key key = texture.key();
            String value = key.value();
            String atlasName = value.contains("/") ?
                    value.substring(0, value.indexOf("/")) : "";
            if (textures.contains(atlasName)) {
                key = Key.key(key.namespace(), value.replaceAll(".png", ""));
                atlasSources.add(AtlasSource.single(key));
            }
        });
        resourcePack.atlas(Atlas.atlas(Key.key("minecraft", "blocks"), atlasSources));
    }
}
