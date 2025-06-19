package net.unnamed.service.command;

import net.unnamed.common.nats.NatsManager;
import net.unnamed.service.command.api.ConsoleCommandSender;
import net.unnamed.service.command.api.packet.CommandResponsePacket;
import net.unnamed.service.command.api.packet.ExecuteCommandPacket;
import net.unnamed.service.command.api.packet.RegisterCommandPacket;
import net.unnamed.service.command.api.packet.TabCompletePacket;
import net.unnamed.service.command.listener.ExecuteCommandListener;
import net.unnamed.service.command.listener.RegisterCommandListener;
import net.unnamed.service.command.listener.TabCompleteListener;
import net.unnamed.service.common.PlatformService;

import java.time.Duration;

public class CommandService extends PlatformService {
    private CommandRegistry commandRegistry = new CommandRegistry();
    private ConsoleCommandSender commandSender = new ConsoleCommandSender(logger);

    @Override
    public void onLoad() {
        logger.info("Loading command service");

        packetRegistry.registerPacket(ExecuteCommandPacket.ID, ExecuteCommandPacket.class);
        packetRegistry.registerPacket(RegisterCommandPacket.ID, RegisterCommandPacket.class);
        packetRegistry.registerPacket(TabCompletePacket.ID, TabCompletePacket.class);

        packetRegistry.subscribe("command.packets");

        packetRegistry.registerListener(RegisterCommandPacket.class, new RegisterCommandListener(commandRegistry));
        packetRegistry.registerListener(ExecuteCommandPacket.class, new ExecuteCommandListener(commandRegistry));
        packetRegistry.registerListener(TabCompletePacket.class, new TabCompleteListener(commandRegistry));
    }

    @Override
    public void onInput(String input) {
        if (input == null || input.isBlank()) return;

        String trimmedInput = input.trim();
        String[] parts = trimmedInput.split("\\s+");

        String command = parts[0];
        String[] args = new String[parts.length - 1];
        if (parts.length > 1) {
            System.arraycopy(parts, 1, args, 0, args.length);
        }

        NatsManager.INSTANCE.request("command.packets",
                new ExecuteCommandPacket(commandSender.getName(), commandSender.isConsole(), command, args),
                CommandResponsePacket.class,
                Duration.ofSeconds(5L)
        ).whenComplete((response, throwable) -> {
            logger.info("Received command response: " + response.getMessage());
        });
    }

    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }
}
