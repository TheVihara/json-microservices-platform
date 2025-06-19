package net.unnamed.service.command.api.builder;

import net.unnamed.service.command.api.CommandExecutor;
import net.unnamed.service.command.api.TabCompleter;
import net.unnamed.service.command.api.condition.CommandCondition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandBuilder {
    private String name;
    private List<String> aliases = new ArrayList<>();
    private String description = "";
    private String usage = "";
    private String permission = "";
    private int cooldown = 0;
    private boolean async = false;
    private CommandExecutor executor;
    private TabCompleter tabCompleter;
    private List<CommandCondition> conditions = new ArrayList<>();

    public static CommandBuilder create(String name) {
        return new CommandBuilder().name(name);
    }

    public CommandBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CommandBuilder aliases(String... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
        return this;
    }

    public CommandBuilder description(String description) {
        this.description = description;
        return this;
    }

    public CommandBuilder usage(String usage) {
        this.usage = usage;
        return this;
    }

    public CommandBuilder permission(String permission) {
        this.permission = permission;
        return this;
    }

    public CommandBuilder cooldown(int seconds) {
        this.cooldown = seconds;
        return this;
    }

    public CommandBuilder async() {
        this.async = true;
        return this;
    }

    public CommandBuilder executor(CommandExecutor executor) {
        this.executor = executor;
        return this;
    }

    public CommandBuilder tabCompleter(TabCompleter tabCompleter) {
        this.tabCompleter = tabCompleter;
        return this;
    }

    public CommandBuilder condition(CommandCondition condition) {
        this.conditions.add(condition);
        return this;
    }

}