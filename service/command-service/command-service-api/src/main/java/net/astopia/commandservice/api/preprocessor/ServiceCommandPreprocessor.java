package net.astopia.commandservice.api.preprocessor;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.astopia.commandservice.api.manager.ServiceCommandManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.execution.preprocessor.CommandPreprocessingContext;
import org.incendo.cloud.execution.preprocessor.CommandPreprocessor;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ServiceCommandPreprocessor<B, C> implements CommandPreprocessor<C> {
    ServiceCommandManager<C> commandManager;
    SenderMapper<B, C> mapper;

    @Override
    public void accept(@NonNull CommandPreprocessingContext<C> ctx) {
/*        ctx.commandContext().store(
                ServiceCommandContextKeys.SENDER_SCHEDULER_EXECUTOR,

        );*/
    }
}
