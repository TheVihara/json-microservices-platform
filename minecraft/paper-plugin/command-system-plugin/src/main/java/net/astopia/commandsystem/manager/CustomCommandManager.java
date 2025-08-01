package net.astopia.commandsystem.manager;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.astopia.commandsystem.api.CommandApi;
import org.incendo.cloud.annotations.AnnotationParser;

import java.util.HashSet;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class CustomCommandManager implements CommandApi {
    HashSet<String> disabledCommands = new HashSet<>();
    AnnotationParser<CommandSourceStack> annotationParser;

    @Override
    public void registerCommand(Object object) {
        annotationParser.parse(object);
    }

    @Override
    public void disableCommand(String name) {
        disabledCommands.add(name);
    }

    @Override
    public void enableCommand(String name) {
        disabledCommands.remove(name);
    }

    @Override
    public boolean isCommandEnabled(String name) {
        return !disabledCommands.contains(name);
    }
}
