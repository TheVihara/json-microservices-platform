package net.unnamed.service.command.api.condition;

import net.unnamed.service.command.api.CommandSender;

public class PlayerOnlyCondition extends CommandCondition {
    @Override
    public boolean check(CommandSender sender, String[] args) {
        return !sender.isConsole();
    }

    @Override
    public String getFailureMessage() {
        return "This command can only be used by players.";
    }
}