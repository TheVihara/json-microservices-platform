package net.unnamed.service.pack.texture.meta.config;

import de.bsommerfeld.jshepherd.annotation.Key;
import de.bsommerfeld.jshepherd.core.ConfigurablePojo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class AnimationMetaConfig extends ConfigurablePojo<AnimationMetaConfig> {
    @Key("frame-time")
    int frameTime;
}
