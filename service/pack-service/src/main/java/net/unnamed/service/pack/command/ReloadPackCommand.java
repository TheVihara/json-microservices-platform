package net.unnamed.service.pack.command;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.astopia.commandservice.api.CommandSender;
import net.unnamed.service.pack.PackManager;
import org.incendo.cloud.annotations.Command;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackWriter;

import java.io.File;
import java.util.logging.Logger;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ReloadPackCommand {
    static Logger logger = Logger.getLogger("ReloadPackCommand");
    PackManager packManager;

    @Command("reloadpack")
    public void reloadCommand(CommandSender sender) {
        ResourcePack resourcePack = packManager.generatePack();
        MinecraftResourcePackWriter.minecraft().writeToZipFile(
                new File("astopia-resource-pack.zip"),
                resourcePack
        );

        logger.info("Pack reloaded...");
    }
}
