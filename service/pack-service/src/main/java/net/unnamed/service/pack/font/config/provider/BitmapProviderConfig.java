package net.unnamed.service.pack.font.config.provider;

import de.bsommerfeld.jshepherd.annotation.Key;
import de.bsommerfeld.jshepherd.core.ConfigurablePojo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class BitmapProviderConfig extends ConfigurablePojo<BitmapProviderConfig> {
    @Key("key")
    String key;

    @Key("namespace")
    String namespace;

    @Key("value")
    String value;

    @Key("height")
    int height;

    @Key("width")
    int width;

    @Key("ascent")
    int ascent;

    @Key("characters")
    List<String> characters;

    @Key("offset")
    List<Integer> offset;
}
