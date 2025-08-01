package net.astopia.commandservice.api.extractor;

import net.astopia.commandservice.api.CommandSender;
import net.astopia.commandservice.api.packet.CommandSuggestionRequestPacket;

import java.util.function.Function;

public class CommandSuggestionSenderExecutor implements Function<CommandSuggestionRequestPacket, CommandSender> {
    @Override
    public CommandSender apply(CommandSuggestionRequestPacket commandSuggestionRequestPacket) {
        return new CommandSender() {
            @Override
            public boolean hasPermission(String permission) {
                return true;
            }

            @Override
            public boolean isConsole() {
                return true;
            }
        };
    }
}
