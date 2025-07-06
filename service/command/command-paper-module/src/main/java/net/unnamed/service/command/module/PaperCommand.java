package net.unnamed.service.command.module;

import net.unnamed.service.command.api.AnnotationCommandRegistry;
import net.unnamed.service.command.api.CommandInfo;
import net.unnamed.service.command.api.CommandResult;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PaperCommand extends Command {
    private final AnnotationCommandRegistry registry;
    private final CommandInfo commandInfo;

    public PaperCommand(CommandInfo commandInfo, AnnotationCommandRegistry registry) {
        super(commandInfo.getName(), commandInfo.getDescription(), commandInfo.getName(), commandInfo.getAliases());
        this.commandInfo = commandInfo;
        this.registry = registry;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String @NotNull [] args) {
        CommandResult result = registry.executeCommand(new PaperCommandSender(sender), commandInfo.getName(), args);
        if (!result.success().isSuccess()) {
            sender.sendMessage(result.getMessage());
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return registry.tabComplete(new PaperCommandSender(sender), commandInfo.getName(), args);
    }
}
