package net.unnamed.service.pack.model.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.unnamed.common.config.YamlConfig;

import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ModelConfig extends YamlConfig<ModelConfig> {

    String parent;

    Map<String, String> textures;
}
