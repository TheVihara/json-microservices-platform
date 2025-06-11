package net.unnamed.service.command;

import net.unnamed.command.api.proto.Command;
import net.unnamed.service.command.listener.CommandInfoListener;
import net.unnamed.service.common.PlatformService;

public class CommandService extends PlatformService {
    private CommandRegistry commandRegistry;

    @Override
    public void load() {
        logger.info("Loading command service");
        this.commandRegistry = new CommandRegistry();

        packetRegistry.registerListener(Command.CommandInfo.class, new CommandInfoListener(commandRegistry));
    }

    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }
}
