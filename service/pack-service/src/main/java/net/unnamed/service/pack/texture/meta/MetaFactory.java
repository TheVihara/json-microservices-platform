package net.unnamed.service.pack.texture.meta;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.unnamed.service.pack.texture.meta.config.AnimationMetaAdapter;
import net.unnamed.service.pack.texture.meta.config.MetadataConfig;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.metadata.MetadataPart;
import team.unnamed.creative.metadata.animation.AnimationMeta;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MetaFactory {
    public Metadata createMetadata(MetadataConfig config) {
        Metadata.Builder builder = Metadata.metadata();

        if (config.getAnimation() != null) {
            builder.addPart(createAnimationPart(config.getAnimation()));
        }

        return builder.build();
    }

    public MetadataPart createAnimationPart(AnimationMetaAdapter animationMetaAdapter) {
        return AnimationMeta.animation()
                .frameTime(animationMetaAdapter.getFrameTime())
                .build();
    }
}
