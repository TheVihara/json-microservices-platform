package net.unnamed.service.command;

import net.unnamed.service.common.PlatformService;

public class CommandService extends PlatformService {
    public CommandService() {
        super(
                "Command",
                "Handles all service commands"
        );
    }

    @Override
    public void load() {
        logger.info("Loading command service");
    }
}
