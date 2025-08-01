package net.astopia.velocitycommandsystem.coordinator;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import net.astopia.commandservice.api.packet.CommandExecutionPacket;
import net.astopia.commandservice.api.packet.CommandSuggestionRequestPacket;
import net.astopia.commandservice.api.packet.CommandSuggestionResponsePacket;
import net.unnamed.common.nats.NatsManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandTree;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.execution.CommandResult;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.meta.CommandMeta;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionMapper;
import org.incendo.cloud.suggestion.Suggestions;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CustomExecutionCoordinator implements ExecutionCoordinator<CommandSource> {
    ExecutionCoordinator<CommandSource> simpleCoordinator = ExecutionCoordinator.simpleCoordinator();

    @Override
    public @NonNull CompletableFuture<CommandResult<CommandSource>> coordinateExecution(
            @NonNull CommandTree<CommandSource> commandTree,
            @NonNull CommandContext<CommandSource> commandContext,
            @NonNull CommandInput commandInput) {

        // Parse the command first to get the command object
        return commandTree.parse(commandContext, commandInput, Runnable::run)
                .thenCompose(command -> {
                    if (command == null) {
                        return simpleCoordinator.coordinateExecution(commandTree, commandContext, commandInput);
                    }

                    CommandMeta commandMeta = command.commandMeta();
                    String service = commandMeta.getOrDefault("service", null);

                    if (service == null) {
                        return simpleCoordinator.coordinateExecution(commandTree, commandContext, commandInput);
                    }

                    CommandSource commandSource = commandContext.sender();
                    CommandExecutionPacket executionPacket;

                    if (commandSource instanceof ConsoleCommandSource) {
                        executionPacket = new CommandExecutionPacket(
                                null,
                                "Console",
                                true,
                                commandInput.input(),
                                ""
                        );
                    } else {
                        Player player = (Player) commandSource;
                        executionPacket = new CommandExecutionPacket(
                                player.getUniqueId(),
                                player.getUsername(),
                                false,
                                commandInput.input(),
                                ""
                        );
                    }

                    NatsManager.INSTANCE.publish("command_service.packets", executionPacket);
                    return CompletableFuture.completedFuture(CommandResult.of(commandContext));
                });
    }

    @Override
    public @NonNull <S extends Suggestion> CompletableFuture<@NonNull Suggestions<CommandSource, S>> coordinateSuggestions(
            @NonNull CommandTree<CommandSource> commandTree,
            @NonNull CommandContext<CommandSource> commandContext,
            @NonNull CommandInput commandInput,
            @NonNull SuggestionMapper<S> mapper) {

        // Parse the command first to get the command object
        return commandTree.parse(commandContext, commandInput, Runnable::run)
                .thenCompose(command -> {
                    if (command == null) {
                        return simpleCoordinator.coordinateSuggestions(commandTree, commandContext, commandInput, mapper);
                    }

                    CommandMeta commandMeta = command.commandMeta();
                    String service = commandMeta.getOrDefault("service", null);

                    if (service == null) {
                        return simpleCoordinator.coordinateSuggestions(commandTree, commandContext, commandInput, mapper);
                    }

                    CommandSource commandSource = commandContext.sender();
                    CommandSuggestionRequestPacket suggestionRequestPacket;

                    if (commandSource instanceof ConsoleCommandSource) {
                        suggestionRequestPacket = new CommandSuggestionRequestPacket(
                                null,
                                "Console",
                                true,
                                commandInput.input(),
                                ""
                        );
                    } else {
                        Player player = (Player) commandSource;
                        suggestionRequestPacket = new CommandSuggestionRequestPacket(
                                player.getUniqueId(),
                                player.getUsername(),
                                false,
                                commandInput.input(),
                                ""
                        );
                    }

                    return NatsManager.INSTANCE.request(
                            "command_service.packets",
                            suggestionRequestPacket,
                            CommandSuggestionResponsePacket.class,
                            Duration.ofSeconds(2)
                    ).thenApply(responsePacket -> {
                        if (responsePacket == null) {
                            return Suggestions.create(commandContext, List.of(), commandInput);
                        }

                        return Suggestions.create(
                                commandContext,
                                responsePacket.getSuggestions().stream()
                                        .map(Suggestion::suggestion)
                                        .map(mapper::map)
                                        .toList(),
                                commandInput);
                    });
                });
    }
}