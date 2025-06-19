package net.unnamed.service.player;

import com.alibaba.fastjson2.JSON;
import net.unnamed.common.nats.NatsManager;
import net.unnamed.service.command.api.AnnotationCommandRegistry;
import net.unnamed.service.command.api.CommandInfo;
import net.unnamed.service.command.api.ConsoleCommandSender;
import net.unnamed.service.command.api.listener.ExecuteCommandListener;
import net.unnamed.service.command.api.packet.CommandResponsePacket;
import net.unnamed.service.command.api.packet.ExecuteCommandPacket;
import net.unnamed.service.command.api.packet.RegisterCommandPacket;
import net.unnamed.service.command.api.packet.TabCompletePacket;
import net.unnamed.service.common.PlatformService;
import net.unnamed.service.player.command.KickPlayerCommand;

import java.time.Duration;

public class PlayerService extends PlatformService {
    private final AnnotationCommandRegistry commandRegistry = new AnnotationCommandRegistry();
    private final ConsoleCommandSender commandSender = new ConsoleCommandSender(logger);

    @Override
    public void onLoad() {
        logger.info("Loading player service");

        packetRegistry.registerPacket(ExecuteCommandPacket.ID, ExecuteCommandPacket.class);
        packetRegistry.registerPacket(TabCompletePacket.ID, TabCompletePacket.class);

        packetRegistry.subscribe("player.commands");

        registerCommands(
                new KickPlayerCommand()
        );

        packetRegistry.registerListener(ExecuteCommandPacket.class, new ExecuteCommandListener(commandRegistry));
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

    public void registerCommands(Object... commandHandlers) {
        for (Object handler : commandHandlers) {
            commandRegistry.registerCommands(handler);
            autoRegisterLegacyCommands(handler);
        }
    }

    private void autoRegisterLegacyCommands(Object handler) {
        Class<?> clazz = handler.getClass();

        for (java.lang.reflect.Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(net.unnamed.service.command.api.annotation.Command.class)) {
                net.unnamed.service.command.api.annotation.Command cmd =
                        method.getAnnotation(net.unnamed.service.command.api.annotation.Command.class);

                CommandInfo legacyInfo = new CommandInfo(
                        cmd.name(),
                        java.util.Arrays.asList(cmd.aliases()),
                        cmd.description(),
                        "player", // Service identifier
                        true
                );

                logger.info("Publishing legacy command " + JSON.toJSONString(legacyInfo));
                NatsManager.INSTANCE.publish("command.packets", new RegisterCommandPacket(legacyInfo));
            }
        }
    }
}
