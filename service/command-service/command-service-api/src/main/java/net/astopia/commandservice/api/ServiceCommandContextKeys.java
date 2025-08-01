package net.astopia.commandservice.api;

import io.leangen.geantyref.TypeToken;
import java.util.concurrent.Executor;
import org.apiguardian.api.API;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.key.CloudKey;


public final class ServiceCommandContextKeys {
    public static final CloudKey<CommandSender> SERVICE_COMMAND_SENDER = CloudKey.of(
            "ServiceCommandSender",
            TypeToken.get(CommandSender.class)
    );

    @API(status = API.Status.STABLE, since = "2.0.0")
    public static final CloudKey<Executor> SENDER_SCHEDULER_EXECUTOR = CloudKey.of(
            "SenderSchedulerExecutor", Executor.class);

    private ServiceCommandContextKeys() {
    }
}