package net.astopia.velocitycommandsystem.listener;

import com.sun.jdi.connect.Connector;
import com.velocitypowered.api.command.CommandSource;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.astopia.commandservice.api.packet.RegisterCommandPacket;
import net.kyori.adventure.text.Component;
import net.unnamed.common.packet.PacketListener;
import org.incendo.cloud.Command;
import org.incendo.cloud.key.CloudKey;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.velocity.VelocityCommandManager;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RegisterCommandListener implements PacketListener<RegisterCommandPacket> {
    VelocityCommandManager<CommandSource> velocityCommandManager;

    private static final CloudKey<String> SERVICE_KEY = CloudKey.of("service", String.class);

    @Override
    public void onPacket(RegisterCommandPacket packet) {
        String name = packet.getCommand();
        List<String> aliases = packet.getAliases();

        Command.Builder<CommandSource> commandBuilder = velocityCommandManager.commandBuilder(name, aliases.toArray(new String[0]));

        Command<CommandSource> command = commandBuilder
                .meta(SERVICE_KEY, packet.getServiceContext())
                .optional("args", StringParser.greedyStringParser())
                .handler(context -> {
                    CommandSource sender = context.sender();
                    sender.sendMessage(Component.text("Command " + name + " executed!"));
                })
                .build();

        velocityCommandManager.command(command);
    }
}
