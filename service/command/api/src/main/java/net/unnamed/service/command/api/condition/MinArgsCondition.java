package net.unnamed.service.command.api.condition;

import net.unnamed.service.command.api.CommandSender;

public class MinArgsCondition extends CommandCondition {
    private final int minArgs;

    public MinArgsCondition(int minArgs) {
        this.minArgs = minArgs;
    }

    @Override
    public boolean check(CommandSender sender, String[] args) {
        return args.length >= minArgs;
    }

    @Override
    public String getFailureMessage() {
        return "This command requires at least " + minArgs + " arguments.";
    }
}