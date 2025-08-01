package net.astopia.knowledgesystem.config;

import de.bsommerfeld.jshepherd.annotation.Key;
import de.bsommerfeld.jshepherd.core.ConfigurablePojo;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class KnowledgeConfig extends ConfigurablePojo<KnowledgeConfig> {
    @Key("key")
    String key;

    @Key("display-name")
    String displayName;

    @Key("material")
    Material material;

    @Key("wiki-url")
    String wikiUrl;

    @Key("position")
    int position;
}
