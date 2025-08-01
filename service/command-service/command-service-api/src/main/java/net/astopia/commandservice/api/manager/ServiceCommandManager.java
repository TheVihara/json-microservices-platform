package net.astopia.commandservice.api.manager;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.astopia.commandservice.api.CommandSender;
import net.astopia.commandservice.api.ServiceCommandContextKeys;
import net.astopia.commandservice.api.listener.CommandExecutionListener;
import net.astopia.commandservice.api.listener.CommandSuggestionListener;
import net.astopia.commandservice.api.packet.CommandExecutionPacket;
import net.astopia.commandservice.api.packet.CommandSuggestionRequestPacket;
import net.astopia.commandservice.api.packet.CommandSuggestionResponsePacket;
import net.astopia.commandservice.api.packet.RegisterCommandPacket;
import net.astopia.commandservice.api.preprocessor.ServiceCommandPreprocessor;
import net.unnamed.common.packet.PacketRegistry;
import net.unnamed.service.common.Service;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.CloudCapability;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.SenderMapperHolder;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.internal.CommandRegistrationHandler;

import java.util.function.Function;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ServiceCommandManager<C> extends CommandManager<C> implements SenderMapperHolder<CommandSender, C> {
    Service service;
    SenderMapper<CommandSender, C> senderMapper;
    Function<CommandExecutionPacket, C> commandExecutionSenderExtractor;
    Function<CommandSuggestionRequestPacket, C> commandSuggestionSenderExtractor;

    public static <C> Builder<C> builder(final SenderMapper<CommandSender, C> senderMapper) {
        return new Builder<>(senderMapper);
    }

    public static Builder<CommandSender> builder() {
        return new Builder<>(SenderMapper.identity());
    }

    private ServiceCommandManager(
            final @NonNull Service service,
            final @NonNull ExecutionCoordinator<C> executionCoordinator,
            final @NonNull CommandRegistrationHandler<C> commandRegistrationHandler,
            final @NonNull SenderMapper<CommandSender, C> senderMapper,
            final @NonNull Function<CommandExecutionPacket, C> commandExecutionSenderExtractor,
            final @NonNull Function<CommandSuggestionRequestPacket, C> commandSuggestionSenderExtractor
    ) {
        super(executionCoordinator, commandRegistrationHandler);
        this.service = service;
        this.senderMapper = senderMapper;
        this.commandExecutionSenderExtractor = commandExecutionSenderExtractor;
        this.commandSuggestionSenderExtractor = commandSuggestionSenderExtractor;


        this.registerCapability(CloudCapability.StandardCapabilities.ROOT_COMMAND_DELETION);

      /*  this.registerDefaultExceptionHandlers();*/

        this.registerCommandPreProcessor(ctx -> {
            ctx.commandContext().store(
                ServiceCommandContextKeys.SERVICE_COMMAND_SENDER,
                this.senderMapper().reverse(ctx.commandContext().sender())
            );

            /*ctx.commandContext().store(
                    "service",
                    service.getName()
            );*/
        });

        this.registerCommandPreProcessor(new ServiceCommandPreprocessor<>(
                this,
                this.senderMapper()
        ));


        PacketRegistry packetRegistry = service.getPacketRegistry();
        packetRegistry.subscribe("command_service.packets");
        packetRegistry.subscribe(service.getName() + "_service.commands");

        packetRegistry.registerPacket(CommandExecutionPacket.ID, CommandExecutionPacket.class);
        packetRegistry.registerPacket(CommandSuggestionRequestPacket.ID, CommandSuggestionRequestPacket.class);
        packetRegistry.registerPacket(CommandSuggestionResponsePacket.ID, CommandSuggestionResponsePacket.class);
        packetRegistry.registerPacket(RegisterCommandPacket.ID, RegisterCommandPacket.class);

        packetRegistry.registerListener(CommandExecutionPacket.class, new CommandExecutionListener<C>(
                this,
                commandExecutionSenderExtractor
        ));
        packetRegistry.registerListener(CommandSuggestionRequestPacket.class, new CommandSuggestionListener<C>(
                this,
                commandSuggestionSenderExtractor
        ));
    }

    @Override
    public final boolean hasPermission(final @NonNull C sender, final @NonNull String permission) {
        return this.senderMapper().reverse(sender).hasPermission(permission);
    }

    @Override
    public final @NonNull SenderMapper<CommandSender, C> senderMapper() {
        return this.senderMapper;
    }

/*    private void registerDefaultExceptionHandlers() {
        this.registerDefaultExceptionHandlers(
                triplet -> this.senderMapper().reverse(triplet.first().sender()).getSender()
                        .sendMessage(Component.text(
                                triplet.first().formatCaption(triplet.second(), triplet.third()),
                                NamedTextColor.RED
                        )),
                pair -> this.owningPlugin().getLogger().log(Level.SEVERE, pair.first(), pair.second())
        );
    }*/

    public static final class Builder<C> {
        private final SenderMapper<CommandSender, C> senderMapper;

        private Builder(final SenderMapper<CommandSender, C> senderMapper) {
            this.senderMapper = senderMapper;
        }

        public RegistrationHandlerBuilder<C> executionCoordinator(ExecutionCoordinator<C> executionCoordinator) {
            return new RegistrationHandlerBuilder<C>(senderMapper, executionCoordinator);
        }
    }

    public static final class RegistrationHandlerBuilder<C> {
        private final SenderMapper<CommandSender, C> senderMapper;
        private final ExecutionCoordinator<C> executionCoordinator;

        public RegistrationHandlerBuilder(
                SenderMapper<CommandSender, C> senderMapper,
                ExecutionCoordinator<C> executionCoordinator
        ) {
            this.senderMapper = senderMapper;
            this.executionCoordinator = executionCoordinator;
        }

        public ExtractorBuilder<C> commandRegistrationHandler(CommandRegistrationHandler<C> commandRegistrationHandler) {
            return new ExtractorBuilder<>(senderMapper, executionCoordinator, commandRegistrationHandler);
        }
    }

    public static final class ExtractorBuilder<C> {
        private final SenderMapper<CommandSender, C> senderMapper;
        private ExecutionCoordinator<C> executionCoordinator;
        private CommandRegistrationHandler<C> commandRegistrationHandler;
        private Function<CommandExecutionPacket, C> commandExecutionSenderExtractor;
        private Function<CommandSuggestionRequestPacket, C> commandSuggestionSenderExtractor;

        private ExtractorBuilder(final SenderMapper<CommandSender, C> senderMapper,
                                 ExecutionCoordinator<C> executionCoordinator,
                                 CommandRegistrationHandler<C> commandRegistrationHandler) {
            this.senderMapper = senderMapper;
            this.executionCoordinator = executionCoordinator;
            this.commandRegistrationHandler = commandRegistrationHandler;
        }

        public ExtractorBuilder<C> extractor(
                Function<CommandExecutionPacket, C> commandExecutionSenderExtractor,
                Function<CommandSuggestionRequestPacket, C> commandSuggestionSenderExtractor
        ) {
            this.commandExecutionSenderExtractor = commandExecutionSenderExtractor;
            this.commandSuggestionSenderExtractor = commandSuggestionSenderExtractor;
            return this;
        }

        public @NonNull ServiceCommandManager<C> build(Service service) {
            return new ServiceCommandManager<>(
                    service,
                    executionCoordinator,
                    commandRegistrationHandler,
                    senderMapper,
                    commandExecutionSenderExtractor,
                    commandSuggestionSenderExtractor
            );
        }
    }
}
