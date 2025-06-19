package net.unnamed.service.command.api.listener;

import net.unnamed.common.nats.NatsManager;
import net.unnamed.common.packet.PacketListener;
import net.unnamed.service.command.api.*;
import net.unnamed.service.command.api.packet.CommandResponsePacket;
import net.unnamed.service.command.api.packet.ExecuteCommandPacket;

import java.util.logging.Logger;

public class ExecuteCommandListener implements PacketListener<ExecuteCommandPacket> {
    private final AnnotationCommandRegistry annotationRegistry;

    public ExecuteCommandListener(AnnotationCommandRegistry annotationRegistry) {
        this.annotationRegistry = annotationRegistry;
    }

    @Override
    public void onPacket(ExecuteCommandPacket packet) {
        String commandName = packet.getCommand();

        System.out.println("Command: " + commandName);

        CommandSender commandSender;

        if (packet.isConsole()) {
            commandSender = new ConsoleCommandSender(Logger.getLogger("ConsoleCommandSender"));
        } else {
            commandSender = new ConsoleCommandSender(Logger.getLogger("CommandSender"));
        }

            // First try annotation-based commands
        try {
            CommandResult result = annotationRegistry.executeCommand(commandSender, commandName, packet.getArgs());
            if (result.isSuccess()) {
                NatsManager.INSTANCE.respond(packet.getOriginalMessage(), new CommandResponsePacket(
                        CommandResponse.SUCCESS,
                        result.getMessage()
                ));
                return;
            } else {
                // If it's a known command but failed, send the error
                NatsManager.INSTANCE.respond(packet.getOriginalMessage(), new CommandResponsePacket(
                        CommandResponse.ERROR,
                        result.getMessage()
                ));
                return;
            }
        } catch (Exception e) {
            // Command not found in annotation registry, try legacy
        }
    }
}