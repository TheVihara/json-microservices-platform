package net.unnamed.service.pack.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.key.Key;
import net.unnamed.common.config.YamlConfig;
import net.unnamed.service.pack.item.config.ItemModelConfig;
import net.unnamed.service.pack.item.factory.ItemFactory;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.item.Item;

import java.io.File;
import java.nio.file.Path;
import java.util.logging.Logger;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ItemManager {
    static Logger logger = Logger.getLogger("ItemManager");
    ItemFactory itemFactory = new ItemFactory();

    public void scan(ResourcePack resourcePack, Path dataFolder) {
        File folder = dataFolder.resolve("items").toFile();
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File[] files = folder.listFiles();
        if (files == null) {
            logger.warning("No files found in items folder: " + folder.getAbsolutePath());
            return;
        }

        String namespace = dataFolder.toFile().getName();
        generate(resourcePack, namespace, files);
    }

    private void generate(ResourcePack resourcePack, String namespace, File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                continue;
            }

            if (file.getName().endsWith(".yml")) {
                try {
                    ItemModelConfig itemModelConfig = YamlConfig.loadSafe(ItemModelConfig.class, file.toPath(), ItemModelConfig::new);

                    String itemName = file.getName().substring(0, file.getName().length() - 4);
                    Key key = Key.key(namespace, itemName);
                    Item item = itemFactory.createItem(key, itemModelConfig);

                    resourcePack.item(item);
                } catch (Exception e) {
                    logger.severe("Failed to load item config from file: " + file.getName() + ", reason: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}
