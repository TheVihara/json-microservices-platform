package net.unnamed.service.command.api.condition;

import net.unnamed.service.command.api.CommandSender;

public class MaxArgsCondition extends CommandCondition {
    private final int maxArgs;

    public MaxArgsCondition(int maxArgs) {
        this.maxArgs = maxArgs;
    }

    @Override
    public boolean check(CommandSender sender, String[] args) {
        return args.length <= maxArgs;
    }

    @Override
    public String getFailureMessage() {
        return "This command accepts at most " + maxArgs + " arguments.";
    }
}