package net.unnamed.service.pack;

import de.bsommerfeld.jshepherd.core.ConfigurationLoader;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.astopia.commandservice.api.CommandSender;
import net.astopia.commandservice.api.ServiceCommandRegistrationHandler;
import net.astopia.commandservice.api.extractor.CommandExecutionSenderExtractor;
import net.astopia.commandservice.api.extractor.CommandSuggestionSenderExecutor;
import net.astopia.commandservice.api.manager.ServiceCommandManager;
import net.unnamed.common.config.CustomYamlPersistenceDelegateFactory;
import net.unnamed.common.database.MySqlConfig;
import net.unnamed.common.database.mysql.MySqlDatabase;
import net.unnamed.service.common.PlatformService;
import net.unnamed.service.pack.command.ReloadPackCommand;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.internal.CommandRegistrationHandler;
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
    @NonFinal ServiceCommandManager<CommandSender> serviceCommandManager;

    @Override
    public void onLoad() {
        serviceCommandManager = ServiceCommandManager.builder()
                .executionCoordinator(ExecutionCoordinator.simpleCoordinator())
                .commandRegistrationHandler(new ServiceCommandRegistrationHandler<>(this))
                .extractor(new CommandExecutionSenderExtractor(), new CommandSuggestionSenderExecutor())
                .build(this);

        AnnotationParser<CommandSender> annotationParser = new AnnotationParser<>(serviceCommandManager, CommandSender.class);
        annotationParser.parse(new ReloadPackCommand(packManager));

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
