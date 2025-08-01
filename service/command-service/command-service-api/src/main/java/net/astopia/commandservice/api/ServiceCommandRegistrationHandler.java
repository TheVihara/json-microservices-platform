package net.astopia.commandservice.api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.astopia.commandservice.api.packet.RegisterCommandPacket;
import net.unnamed.common.nats.NatsManager;
import net.unnamed.service.common.Service;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.Command;
import org.incendo.cloud.component.CommandComponent;
import org.incendo.cloud.internal.CommandRegistrationHandler;

import java.util.Collection;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ServiceCommandRegistrationHandler<C> implements CommandRegistrationHandler<C> {
    Service service;

    @Override
    public boolean registerCommand(@NonNull Command<C> command) {
        CommandComponent<C> rootComponent = command.rootComponent();
        String commandName = rootComponent.name();
        Collection<String> aliases = rootComponent.aliases();

        NatsManager.INSTANCE.publish("command_service.packets", new RegisterCommandPacket(
                commandName,
                aliases.stream().toList(),
                service.getName().toLowerCase()
        ));
        return true;
    }
}