package net.unnamed.service.command.api;

import java.lang.reflect.Method;
import java.util.List;

class RegisteredSubcommand {
    private final String name;
    private final List<String> aliases;
    private final String description;
    private final String usage;
    private final String permission;
    private final int cooldown;
    private final boolean async;
    private final Object handler;
    private final Method method;

    public RegisteredSubcommand(String name, List<String> aliases, String description, String usage,
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
}