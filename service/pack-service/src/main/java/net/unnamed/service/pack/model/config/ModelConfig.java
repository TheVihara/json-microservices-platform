package net.unnamed.service.pack.model.config;

import de.bsommerfeld.jshepherd.annotation.Key;
import de.bsommerfeld.jshepherd.core.ConfigurablePojo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ModelConfig extends ConfigurablePojo<ModelConfig> {
    @Key("parent")
    String parent;

    @Key("textures")
    Map<String, String> textures;
}
