package net.unnamed.service.gui.module.manager;

import de.bsommerfeld.jshepherd.core.ConfigurationLoader;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.unnamed.service.gui.module.gui.config.KnowledgeConfig;
import org.bukkit.Material;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class KnowledgeManager {
    Map<String, KnowledgeConfig> knowledgeEntries = new HashMap<>();

    public KnowledgeManager(File knowledgeFolder) {
        if (!knowledgeFolder.exists()) {
            knowledgeFolder.mkdirs();
        }

        File[] files = knowledgeFolder.listFiles();

        if (files == null) {
            return;
        }

        for (File file : files) {
            if (!file.getName().endsWith(".yml")) {
                continue;
            }

            Path path = file.toPath();
            KnowledgeConfig knowledgeConfig = ConfigurationLoader.load(path, KnowledgeConfig::new);
            knowledgeEntries.put(knowledgeConfig.getKey(), knowledgeConfig);
        }

        for (int i = 0; i < 100; i++) {
            KnowledgeConfig knowledgeConfig = new KnowledgeConfig(
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    Material.BOOK,
                    "",
                    i++
            );
            knowledgeEntries.put(knowledgeConfig.getKey(), knowledgeConfig);
        }
    }

    public KnowledgeConfig getKnowledgeEntry(String key) {
        return knowledgeEntries.get(key);
    }

    public List<KnowledgeConfig> getKnowledgeEntries() {
        return List.copyOf(knowledgeEntries.values());
    }

    public void registerKnowledgeEntry(KnowledgeConfig knowledgeConfig) {
        knowledgeEntries.put(knowledgeConfig.getKey(), knowledgeConfig);
    }

    public void unregisterKnowledgeEntry(String key) {
        knowledgeEntries.remove(key);
    }
}
