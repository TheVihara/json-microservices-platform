package net.astopia.commandsystem;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.astopia.commandsystem.api.CommandApi;
import net.astopia.commandsystem.coordinator.CustomCommandPreprocessor;
import net.astopia.commandsystem.manager.CustomCommandManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@SuppressWarnings("UnstableApiUsage")
public class CommandSystemPlugin extends JavaPlugin {
    ExecutionCoordinator<CommandSourceStack> executionCoordinator = ExecutionCoordinator.simpleCoordinator();

    @NonFinal PaperCommandManager<CommandSourceStack> paperCommandManager;
    @NonFinal AnnotationParser<CommandSourceStack> annotationParser;
    @NonFinal CustomCommandManager customCommandManager;

    @Override
    public void onEnable() {
        paperCommandManager = PaperCommandManager.builder()
                .executionCoordinator(executionCoordinator)
                .buildOnEnable(this);
        annotationParser = new AnnotationParser(paperCommandManager, CommandSourceStack.class);
        customCommandManager = new CustomCommandManager(annotationParser);

        Bukkit.getServicesManager().register(CommandApi.class, customCommandManager, this, ServicePriority.Normal);

        paperCommandManager.registerCommandPreProcessor(new CustomCommandPreprocessor(customCommandManager));
    }
}
