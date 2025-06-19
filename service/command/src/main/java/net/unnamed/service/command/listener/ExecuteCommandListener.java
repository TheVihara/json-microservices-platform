package net.unnamed.service.command.listener;

import net.unnamed.common.nats.NatsManager;
import net.unnamed.common.packet.PacketListener;
import net.unnamed.service.command.CommandRegistry;
import net.unnamed.service.command.api.CommandInfo;
import net.unnamed.service.command.api.CommandResponse;
import net.unnamed.service.command.api.packet.CommandResponsePacket;
import net.unnamed.service.command.api.packet.ExecuteCommandPacket;
import net.unnamed.service.command.api.packet.TabCompleteResponsePacket;

import java.time.Duration;

public class ExecuteCommandListener implements PacketListener<ExecuteCommandPacket> {
    private final CommandRegistry commandRegistry;

    public ExecuteCommandListener(CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void onPacket(ExecuteCommandPacket packet) {
        String commandName = packet.getCommand();

        // Fall back to legacy command system
        CommandInfo commandInfo = commandRegistry.getCommandInfo(commandName);
        if (commandInfo == null) {
            NatsManager.INSTANCE.respond(packet.getOriginalMessage(), new CommandResponsePacket(
                    CommandResponse.UNKNOWN_COMMAND,
                    "Unknown command: " + commandName
            ));
            return;
        }

        if (!commandInfo.isEnabled()) {
            NatsManager.INSTANCE.respond(packet.getOriginalMessage(), new CommandResponsePacket(
                    CommandResponse.DISABLED,
                    "Command is disabled"
            ));
            return;
        }

        System.out.println("Executing command: " + commandName);
        System.out.println("Sending to: " + commandInfo.getService() + ".commands");

        // Forward to service for legacy commands
        NatsManager.INSTANCE.request(
                commandInfo.getService() + ".commands",
                packet,
                CommandResponsePacket.class,
                Duration.ofSeconds(5)
        ).whenComplete((response, throwable) -> {
            if (throwable != null) {
                // Handle timeout or error
                throwable.printStackTrace();
                return;
            }

            // Send back the response
            NatsManager.INSTANCE.respond(packet.getOriginalMessage(), response);
            System.out.println("SENDING BACK RESPONSE");
        });
    }
}
