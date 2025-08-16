package net.unnamed.service.pack.font.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.unnamed.common.config.YamlConfig;
import net.unnamed.service.pack.font.adapter.*;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class FontConfig extends YamlConfig<FontConfig> {

    @JsonProperty("reference")
    List<ReferenceProviderAdapter> referenceFontProviders;

    @JsonProperty("bitmap")
    List<BitmapProviderAdapter> bitmapFontProviders;

    @JsonProperty("space")
    List<SpaceProviderAdapter> spaceProvider;

    @JsonProperty("true-type")
    List<TrueTypeProviderAdapter> trueTypeFontProviders;

    @JsonProperty("unihex")
    List<UnihexProviderAdapter> unihexFontProviders;
}
