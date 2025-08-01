package net.unnamed.service.pack.font.config.provider;

import de.bsommerfeld.jshepherd.annotation.Key;
import de.bsommerfeld.jshepherd.core.ConfigurablePojo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ReferenceProviderConfig extends ConfigurablePojo<ReferenceProviderConfig> {
    @Key("namespace")
    String namespace;

    @Key("value")
    String value;
}
