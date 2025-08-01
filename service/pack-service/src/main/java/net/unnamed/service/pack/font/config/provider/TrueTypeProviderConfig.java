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
public class TrueTypeProviderConfig extends ConfigurablePojo<TrueTypeProviderConfig> {
    @Key("namespace")
    String namespace;

    @Key("value")
    String value;

    @Key("oversample")
    float oversample;

    @Key("shiftX")
    float shiftX;

    @Key("shiftY")
    float shiftY;

    @Key("size")
    float size;

    @Key("skip")
    List<String> skip = List.of();

    @Key("offset")
    List<Double> offset = List.of();
}
