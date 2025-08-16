package net.unnamed.service.pack.atlas.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.unnamed.common.config.YamlConfig;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class AtlasConfig extends YamlConfig<AtlasConfig> {

    List<String> textures;
}
