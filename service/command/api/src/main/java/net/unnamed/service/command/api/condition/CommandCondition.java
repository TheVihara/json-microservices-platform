package net.unnamed.service.command.api.condition;

import net.unnamed.service.command.api.CommandSender;

public abstract class CommandCondition {
    public abstract boolean check(CommandSender sender, String[] args);
    public abstract String getFailureMessage();
    public String getName() {
        return this.getClass().getSimpleName();
    }
}