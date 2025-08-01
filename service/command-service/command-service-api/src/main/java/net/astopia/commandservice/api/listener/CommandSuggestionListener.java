package net.astopia.commandservice.api.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.astopia.commandservice.api.manager.ServiceCommandManager;
import net.astopia.commandservice.api.packet.CommandSuggestionRequestPacket;
import net.astopia.commandservice.api.packet.CommandSuggestionResponsePacket;
import net.unnamed.common.nats.NatsManager;
import net.unnamed.common.packet.PacketListener;
import org.incendo.cloud.suggestion.Suggestion;

import java.util.List;
import java.util.function.Function;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CommandSuggestionListener<C> implements PacketListener<CommandSuggestionRequestPacket> {
    ServiceCommandManager<C> commandManager;
    Function<CommandSuggestionRequestPacket, C> mapper;

    @Override
    public void onPacket(CommandSuggestionRequestPacket packet) {
        commandManager.suggestionFactory().suggest(
                        mapper.apply(packet),
                        packet.getCommandInput()
                ).thenAccept(suggestions -> {
                    List<String> suggestionList = suggestions.list().stream()
                            .map(Suggestion::suggestion).toList();

                    NatsManager.INSTANCE.respond(
                            packet.getOriginalMessage(),
                            new CommandSuggestionResponsePacket(suggestionList)
                    );

        });
    }
}
