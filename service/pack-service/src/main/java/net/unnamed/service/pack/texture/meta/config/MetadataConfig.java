package net.unnamed.service.pack.texture.meta.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.unnamed.common.config.YamlConfig;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class MetadataConfig extends YamlConfig<MetadataConfig> {
    AnimationMetaAdapter animation;
}
