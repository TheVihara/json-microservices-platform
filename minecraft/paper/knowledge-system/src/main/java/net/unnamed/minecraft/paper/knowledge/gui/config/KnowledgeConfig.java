package net.unnamed.minecraft.paper.knowledge.gui.config;

import de.bsommerfeld.jshepherd.core.ConfigurablePojo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Getter
@Setter
public class KnowledgeConfig extends ConfigurablePojo<KnowledgeConfig> {
    String key;
    String displayName;
    Material material;
    String wikiUrl;
    int position;
}
