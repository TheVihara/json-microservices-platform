package net.unnamed.service.command.api;

import net.unnamed.service.command.api.annotation.Condition;
import net.unnamed.service.command.api.condition.CommandCondition;
import net.unnamed.service.command.api.condition.PermissionCondition;

import java.lang.reflect.Method;
import java.util.*;

class RegisteredCommand {
    private final String name;
    private final List<String> aliases;
    private final String description;
    private final String usage;
    private final String permission;
    private final int cooldown;
    private final boolean async;
    private final Object handler;
    private final Method method;
    private final Map<String, RegisteredSubcommand> subcommands = new HashMap<>();
    private final List<CommandCondition> conditions = new ArrayList<>();
    private TabCompleter tabCompleter;

    public RegisteredCommand(String name, List<String> aliases, String description, String usage,
                             String permission, int cooldown, boolean async, Object handler, Method method) {
        this.name = name;
        this.aliases = aliases;
        this.description = description;
        this.usage = usage;
        this.permission = permission;
        this.cooldown = cooldown;
        this.async = async;
        this.handler = handler;
        this.method = method;

        if (method.isAnnotationPresent(Condition.class)) {
            Condition conditionAnnotation = method.getAnnotation(Condition.class);
            for (Class<? extends CommandCondition> conditionClass : conditionAnnotation.value()) {
                try {
                    conditions.add(conditionClass.getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (!permission.isEmpty()) {
            conditions.add(new PermissionCondition(permission));
        }
    }

    public String getName() { return name; }
    public List<String> getAliases() { return aliases; }
    public String getDescription() { return description; }
    public String getUsage() { return usage; }
    public String getPermission() { return permission; }
    public int getCooldown() { return cooldown; }
    public boolean isAsync() { return async; }
    public Object getHandler() { return handler; }
    public Method getMethod() { return method; }
    public List<CommandCondition> getConditions() { return conditions; }
    public TabCompleter getTabCompleter() { return tabCompleter; }
    public void setTabCompleter(TabCompleter tabCompleter) { this.tabCompleter = tabCompleter; }

    public void addSubcommand(String name, RegisteredSubcommand subcommand) {
        subcommands.put(name, subcommand);
        for (String alias : subcommand.getAliases()) {
            subcommands.put(alias, subcommand);
        }
    }

    public RegisteredSubcommand getSubcommand(String name) { return subcommands.get(name); }
    public boolean hasSubcommand(String name) { return subcommands.containsKey(name); }
    public Set<String> getSubcommandNames() { return subcommands.keySet(); }
}