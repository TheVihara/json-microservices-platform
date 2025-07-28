package net.unnamed.service.pack;

import de.bsommerfeld.jshepherd.core.ConfigurationLoader;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.unnamed.common.config.CustomYamlPersistenceDelegateFactory;
import net.unnamed.common.database.MySqlConfig;
import net.unnamed.common.database.mysql.MySqlDatabase;
import net.unnamed.service.common.PlatformService;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackWriter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Getter
public class PackService extends PlatformService {
    CustomYamlPersistenceDelegateFactory yamlPersistenceDelegateFactory = new CustomYamlPersistenceDelegateFactory();
    MySqlConfig config = ConfigurationLoader.load(Path.of(".").resolve("config.yml"), MySqlConfig::new);
    MySqlDatabase database = new MySqlDatabase(config);
    PackManager packManager = new PackManager(Paths.get("./pack"), database.getDataSource());

    @Override
    public void onLoad() {
        ResourcePack pack = packManager.generatePack();
        MinecraftResourcePackWriter.minecraft().writeToZipFile(
                new File("astopia-resource-pack.zip"),
                pack
        );
    }

    @Override
    public void onStop() {

    }
}
