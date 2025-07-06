package net.unnamed.service.pack;

import net.unnamed.service.common.PlatformService;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackWriter;

import java.io.File;
import java.nio.file.Paths;

public class PackService extends PlatformService {
    private final PackManager packManager = new PackManager(Paths.get("./pack"));

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
