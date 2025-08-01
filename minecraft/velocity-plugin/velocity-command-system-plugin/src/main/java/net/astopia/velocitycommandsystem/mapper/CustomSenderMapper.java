package net.astopia.velocitycommandsystem.mapper;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.astopia.commandservice.api.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.SenderMapper;

public class CustomSenderMapper implements SenderMapper<CommandSource, CommandSender> {
    @Override
    public @NonNull CommandSender map(@NonNull CommandSource base) {
        return new CommandSenderImpl(base);
    }

    @Override
    public @NonNull CommandSource reverse(@NonNull CommandSender mapped) {
        if (mapped instanceof CommandSenderImpl impl) {
            return impl.getBase();
        }
        throw new IllegalArgumentException("Cannot reverse map a CommandSender that is not a CommandSenderImpl");
    }

    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @Getter
    @RequiredArgsConstructor
    private static class CommandSenderImpl implements CommandSender {
        CommandSource base;

        @Override
        public boolean hasPermission(String permission) {
            return base.hasPermission(permission);
        }

        @Override
        public boolean isConsole() {
            return base instanceof ConsoleCommandSource;
        }

    }
}
