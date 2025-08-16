package net.unnamed.service.pack.model;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.key.Key;
import net.unnamed.common.config.YamlConfig;
import net.unnamed.service.pack.model.config.ModelConfig;
import net.unnamed.service.pack.model.factory.ModelFactory;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.model.Model;

import java.io.File;
import java.nio.file.Path;
import java.util.logging.Logger;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ModelManager {
    static Logger logger = Logger.getLogger("ModelManager");
    ModelFactory modelFactory = new ModelFactory();

    public void scan(ResourcePack resourcePack, Path dataFolder) {
        File folder = dataFolder.resolve("models").toFile();
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File[] files = folder.listFiles();
        if (files == null) {
            logger.warning("No files found in items folder: " + folder.getAbsolutePath());
            return;
        }

        String namespace = dataFolder.toFile().getName();
        generate(resourcePack, folder.toPath(), namespace, files);
    }

    private void generate(ResourcePack resourcePack, Path dataFolder, String namespace, File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                File[] subFiles = file.listFiles();

                if (subFiles == null) {
                    continue;
                }

                generate(resourcePack, dataFolder, namespace, subFiles);
                continue;
            }

            if (file.getName().endsWith(".yml")) {
                try {
                    ModelConfig modelConfig = YamlConfig.loadSafe(ModelConfig.class, file.toPath(), ModelConfig::new);
                    Path filePath = file.toPath();
                    Path relativePath = dataFolder.relativize(filePath);
                    String modelPath = relativePath.toString().replace(File.separatorChar, '/').replaceAll(".yml", "");
                    Key key = Key.key(namespace, modelPath);
                    Model model = modelFactory.createModel(key, modelConfig);

                    resourcePack.model(model);
                } catch (Exception e) {
                    logger.severe("Failed to load item config from file: " + file.getName() + ", reason: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}
