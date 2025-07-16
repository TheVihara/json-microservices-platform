package net.unnamed.service.pack.model;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.unnamed.service.pack.model.factory.ModelFactory;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.model.Model;

import java.io.File;
import java.nio.file.Path;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ModelManager {
    ModelFactory modelFactory = new ModelFactory();
    Path dataFolder;

    public void scan(ResourcePack resourcePack) {
        Path modelFolder = dataFolder.resolve("font");
        if (!modelFolder.toFile().exists()) {
            modelFolder.toFile().mkdirs();
            return;
        }
        File[] files = modelFolder.toFile().listFiles();
        if (files == null) {
            return;
        }
        scan(resourcePack, modelFolder, files);
    }

    private void scan(ResourcePack resourcePack, Path modelFolder, File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                File[] subFiles = file.listFiles();
                if (subFiles == null) {
                    continue;
                }
                scan(resourcePack, modelFolder, subFiles);
            } else {
                Path filePath = file.toPath();
                Path relativePath = modelFolder.relativize(filePath);
                String modelPath = relativePath.toString().replace(File.separatorChar, '/');
                Model foundModel = modelFactory.createModel("astopia", modelPath);
                resourcePack.model(foundModel);
            }
        }
    }
}
