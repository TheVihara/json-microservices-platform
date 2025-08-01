package net.astopia.paperconnector.command;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.astopia.paperconnector.api.module.ModuleDescriptor;
import net.astopia.paperconnector.api.module.ModuleLoader;
import net.astopia.paperconnector.module.ModuleManager;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleCommand implements BasicCommand {
    private final ModuleManager moduleManager;
    private final ModuleLoader moduleLoader;

    public ModuleCommand(ModuleManager moduleManager, ModuleLoader moduleLoader) {
        this.moduleManager = moduleManager;
        this.moduleLoader = moduleLoader;
    }

    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        CommandSender sender = commandSourceStack.getSender();

        if (args.length == 0) {
            sender.sendMessage("Usage: /module <enable|disable|restart|reload|list|info> [module-id]");
            return;
        }

        String action = args[0].toLowerCase();

        switch (action) {
            case "list":
                listModules(sender);
                break;
            case "enable":
                if (args.length < 2) {
                    sender.sendMessage("Usage: /module enable <module-id>");
                    return;
                }
                enableModule(sender, args[1]);
                break;
            case "disable":
                if (args.length < 2) {
                    sender.sendMessage("Usage: /module disable <module-id>");
                    return;
                }
                disableModule(sender, args[1]);
                break;
            case "restart":
                if (args.length < 2) {
                    sender.sendMessage("Usage: /module restart <module-id>");
                    return;
                }
                restartModule(sender, args[1]);
                break;
            case "reload":
                if (args.length < 2) {
                    sender.sendMessage("Usage: /module reload <module-id>");
                    return;
                }
                reloadModule(sender, args[1]);
                break;
            case "info":
                if (args.length < 2) {
                    sender.sendMessage("Usage: /module info <module-id>");
                    return;
                }
                showModuleInfo(sender, args[1]);
                break;
            default:
                sender.sendMessage("Unknown action: " + action);
                return;
        }

        return;
    }

    @Override
    public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(List.of("enable", "disable", "restart", "reload", "list", "info"));
        } else if (args.length == 2) {
            completions.addAll(moduleLoader.getLoadedModules().stream()
                    .map(ModuleDescriptor::getId)
                    .collect(Collectors.toList()));
        }

        return completions.stream()
                .filter(completion -> completion.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }

    private void listModules(CommandSender sender) {
        sender.sendMessage("§6=== Loaded Modules ===");
        for (ModuleDescriptor descriptor : moduleLoader.getLoadedModules()) {
            String status = moduleManager.isModuleEnabled(descriptor.getId()) ? "§aENABLED" : "§cDISABLED";
            sender.sendMessage("§e" + descriptor.getId() + " §7v" + descriptor.getVersion() + " " + status);
        }
    }

    private void enableModule(CommandSender sender, String moduleId) {
        try {
            moduleManager.enableModule(moduleId);
            sender.sendMessage("§aModule " + moduleId + " has been enabled.");
        } catch (Exception e) {
            sender.sendMessage("§cFailed to enable module " + moduleId + ": " + e.getMessage());
        }
    }

    private void disableModule(CommandSender sender, String moduleId) {
        try {
            moduleManager.disableModule(moduleId);
            sender.sendMessage("§aModule " + moduleId + " has been disabled.");
        } catch (Exception e) {
            sender.sendMessage("§cFailed to disable module " + moduleId + ": " + e.getMessage());
        }
    }

    private void restartModule(CommandSender sender, String moduleId) {
        try {
            moduleManager.restartModule(moduleId);
            sender.sendMessage("§aModule " + moduleId + " has been restarted.");
        } catch (Exception e) {
            sender.sendMessage("§cFailed to restart module " + moduleId + ": " + e.getMessage());
        }
    }

    private void reloadModule(CommandSender sender, String moduleId) {
        try {
            moduleLoader.reloadModule(moduleId);
            sender.sendMessage("§aModule " + moduleId + " has been reloaded.");
        } catch (Exception e) {
            sender.sendMessage("§cFailed to reload module " + moduleId + ": " + e.getMessage());
        }
    }

    private void showModuleInfo(CommandSender sender, String moduleId) {
        ModuleDescriptor descriptor = moduleLoader.getModuleDescriptor(moduleId);
        if (descriptor == null) {
            sender.sendMessage("§cModule not found: " + moduleId);
            return;
        }

        sender.sendMessage("§6=== Module Info: " + moduleId + " ===");
        sender.sendMessage("§eVersion: §7" + descriptor.getVersion());
        sender.sendMessage("§eMain Class: §7" + descriptor.getMainClass());
        sender.sendMessage("§eStatus: " + (moduleManager.isModuleEnabled(moduleId) ? "§aENABLED" : "§cDISABLED"));
        sender.sendMessage("§eState: §7" + moduleManager.getModuleState(moduleId));
        if (descriptor.getDependencies().length > 0) {
            sender.sendMessage("§eDependencies: §7" + String.join(", ", descriptor.getDependencies()));
        }
    }
}