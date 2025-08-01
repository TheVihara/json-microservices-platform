package net.unnamed.service.pack.font.config.provider;

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
public class SpaceProviderConfig extends ConfigurablePojo<SpaceProviderConfig> {
    @Key("advances")
    Map<String, Integer> advances;
}
