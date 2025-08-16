package net.astopia.knowledgesystem.config;

import net.astopia.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import net.unnamed.common.config.YamlConfig;
import org.bukkit.Material;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class KnowledgeConfig extends YamlConfig<KnowledgeConfig> {

    String key;

    @JsonProperty("display-name")
    String displayName;

    Material material;

    @JsonProperty("wiki-url")
    String wikiUrl;

    int position;
}
