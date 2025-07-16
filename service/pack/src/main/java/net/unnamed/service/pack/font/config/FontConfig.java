package net.unnamed.service.pack.font.config;

import de.bsommerfeld.jshepherd.annotation.Key;
import de.bsommerfeld.jshepherd.core.ConfigurablePojo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.unnamed.service.pack.font.config.provider.*;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class FontConfig extends ConfigurablePojo<FontConfig> {
    @Key("reference")
    List<ReferenceProviderConfig> referenceFontProviders;

    @Key("bitmap")
    List<BitmapProviderConfig> bitmapFontProviders;

    @Key("space")
    List<SpaceProviderConfig> spaceProvider;

    @Key("true-type")
    List<TrueTypeProviderConfig> trueTypeFontProviders;

    @Key("unihex")
    List<UnihexProviderConfig> unihexFontProviders;
}
