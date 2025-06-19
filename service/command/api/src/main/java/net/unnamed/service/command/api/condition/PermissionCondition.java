package net.unnamed.service.command.api.condition;

import net.unnamed.service.command.api.CommandSender;

public class PermissionCondition extends CommandCondition {
    private final String permission;

    public PermissionCondition(String permission) {
        this.permission = permission;
    }

    @Override
    public boolean check(CommandSender sender, String[] args) {
        return sender.hasPermission(permission);
    }

    @Override
    public String getFailureMessage() {
        return "You don't have permission to use this command.";
    }
}

