package net.unnamed.service.command.api.condition;

import net.unnamed.service.command.api.CommandSender;

public class CustomCondition extends CommandCondition {
    private final java.util.function.BiPredicate<CommandSender, String[]> condition;
    private final String failureMessage;

    public CustomCondition(java.util.function.BiPredicate<CommandSender, String[]> condition, String failureMessage) {
        this.condition = condition;
        this.failureMessage = failureMessage;
    }

    @Override
    public boolean check(CommandSender sender, String[] args) {
        return condition.test(sender, args);
    }

    @Override
    public String getFailureMessage() {
        return failureMessage;
    }
}