package net.astopia.knowledgesystem.factory;

import net.astopia.knowledgesystem.config.KnowledgeConfig;
import net.unnamed.common.config.YamlConfig;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class KnowledgeFactory {

    private final Map<String, KnowledgeConfig> loadedEntries = new HashMap<>();

    public KnowledgeFactory(Path dataPath) {
        File knowledgeFolder = dataPath.resolve("knowledge").toFile();

        if (!knowledgeFolder.exists()) {
            knowledgeFolder.mkdirs();
        }

        File[] files = knowledgeFolder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (!file.getName().endsWith(".yml")) continue;

                Path path = file.toPath();
                KnowledgeConfig knowledgeConfig = YamlConfig.loadSafe(KnowledgeConfig.class, path, KnowledgeConfig::new);
                loadedEntries.put(knowledgeConfig.getKey(), knowledgeConfig);
            }
        }
    }

    public Map<String, KnowledgeConfig> getAllEntries() {
        return loadedEntries;
    }
}