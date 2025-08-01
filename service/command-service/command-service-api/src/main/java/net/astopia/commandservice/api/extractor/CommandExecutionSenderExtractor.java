package net.astopia.commandservice.api.extractor;

import net.astopia.commandservice.api.CommandSender;
import net.astopia.commandservice.api.packet.CommandExecutionPacket;

import java.util.function.Function;

public class CommandExecutionSenderExtractor implements Function<CommandExecutionPacket, CommandSender> {
    @Override
    public CommandSender apply(CommandExecutionPacket commandExecutionPacket) {
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
