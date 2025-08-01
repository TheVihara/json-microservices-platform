package net.astopia.commandsystem.coordinator;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.astopia.commandsystem.manager.CustomCommandManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.execution.preprocessor.CommandPreprocessingContext;
import org.incendo.cloud.execution.preprocessor.CommandPreprocessor;
import org.incendo.cloud.services.State;

import java.util.logging.Logger;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class CustomCommandPreprocessor implements CommandPreprocessor<CommandSourceStack> {
    static Logger logger = Logger.getLogger("CommandPreprocessor");

    CustomCommandManager customCommandManager;

    @Override
    public void accept(@NonNull CommandPreprocessingContext<CommandSourceStack> context) {
        logger.info(context.commandInput().input() + " ACCEPT");
    }

    @Override
    public @NonNull State handle(@NonNull CommandPreprocessingContext<CommandSourceStack> context) {
        logger.info(context.commandInput().input() + " HANDLE");
        return CommandPreprocessor.super.handle(context);
    }
}
