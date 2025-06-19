package net.unnamed.service.command.api;

import net.unnamed.service.command.api.annotation.*;
import net.unnamed.service.command.api.argument.*;
import net.unnamed.service.command.api.condition.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationCommandRegistry {
    private final Map<String, RegisteredCommand> commands = new ConcurrentHashMap<>();
    private final Map<String, String> aliases = new ConcurrentHashMap<>();
    private final Map<String, ArgumentType<?>> argumentTypes = new ConcurrentHashMap<>();
    private final Map<String, Long> cooldowns = new ConcurrentHashMap<>();

    public AnnotationCommandRegistry() {
        registerDefaultArgumentTypes();
    }

    private void registerDefaultArgumentTypes() {
        registerArgumentType(String.class, new StringArgumentType());
        registerArgumentType(Integer.class, new IntegerArgumentType());
        registerArgumentType(int.class, new IntegerArgumentType());
        registerArgumentType(Boolean.class, new BooleanArgumentType());
        registerArgumentType(boolean.class, new BooleanArgumentType());
    }

    public <T> void registerArgumentType(Class<T> type, ArgumentType<T> argumentType) {
        argumentTypes.put(type.getName(), argumentType);
    }

    public void registerCommands(Object commandHandler) {
        Class<?> clazz = commandHandler.getClass();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Command.class)) {
                registerCommand(commandHandler, method);
            }
        }
    }

    private void registerCommand(Object handler, Method method) {
        Command command = method.getAnnotation(Command.class);

        RegisteredCommand registeredCommand = new RegisteredCommand(
                command.name(),
                Arrays.asList(command.aliases()),
                command.description(),
                command.usage(),
                command.permission(),
                command.cooldown(),
                command.async(),
                handler,
                method
        );

        // Register subcommands
        for (Method subMethod : handler.getClass().getDeclaredMethods()) {
            if (subMethod.isAnnotationPresent(Subcommand.class)) {
                Subcommand sub = subMethod.getAnnotation(Subcommand.class);
                registeredCommand.addSubcommand(sub.name(), new RegisteredSubcommand(
                        sub.name(),
                        Arrays.asList(sub.aliases()),
                        sub.description(),
                        sub.usage(),
                        sub.permission(),
                        sub.cooldown(),
                        sub.async(),
                        handler,
                        subMethod
                ));
            }
        }

        // Register tab completers
        for (Method tabMethod : handler.getClass().getDeclaredMethods()) {
            if (tabMethod.isAnnotationPresent(TabComplete.class)) {
                TabComplete tab = tabMethod.getAnnotation(TabComplete.class);
                if (tab.command().equals(command.name())) {
                    registeredCommand.setTabCompleter((sender, args) -> {
                        try {
                            return (List<String>) tabMethod.invoke(handler, sender, args);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return Collections.emptyList();
                        }
                    });
                }
            }
        }

        commands.put(command.name(), registeredCommand);
        for (String alias : command.aliases()) {
            aliases.put(alias, command.name());
        }
    }

    public CommandResult executeCommand(CommandSender sender, String commandName, String[] args) {
        RegisteredCommand command = getCommand(commandName);
        System.out.println("MEOW MEOW");
        if (command == null) {
            return CommandResult.failure("Unknown command: " + commandName);
        }

        System.out.println("WE'RE HERE ONW");

        // Check cooldown
        String cooldownKey = sender.getName() + ":" + commandName;
        if (cooldowns.containsKey(cooldownKey)) {
            System.out.println("OOHH NOOO");
            long timeLeft = cooldowns.get(cooldownKey) - System.currentTimeMillis();
            if (timeLeft > 0) {
                return CommandResult.failure("Command on cooldown. Wait " + (timeLeft / 1000) + " seconds.");
            }
        }

        System.out.println("CHECKIN CONDITIONS");

        // Check conditions
        for (CommandCondition condition : command.getConditions()) {
            System.out.println("CONDITION: " + condition.getName());
            try {
                if (!condition.check(sender, args)) {
                    System.out.println("SHIT SHIT");
                    return CommandResult.failure(condition.getFailureMessage());
                }
            } catch (Exception e) {
                System.out.println("EXCEPTION IN CONDITION: " + e.getMessage());
                e.printStackTrace();
                return CommandResult.failure("An error occurred while checking command conditions.");
            }
        }


        System.out.println("CHECKOUT SUB CMDS OR SUM");

        // Handle subcommands
        if (args.length > 0 && command.hasSubcommand(args[0])) {
            RegisteredSubcommand subcommand = command.getSubcommand(args[0]);
            String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
            return executeSubcommand(sender, command, subcommand, subArgs);
        }

        // Parse arguments
        CommandContext context = new CommandContext(sender, commandName, args);
        System.out.println("CONTEXT YAY");
        try {
            parseArguments(command.getMethod(), context, args);
        } catch (ArgumentParseException e) {
            return CommandResult.failure("Invalid argument: " + e.getMessage());
        }

        // Set cooldown
        if (command.getCooldown() > 0) {
            cooldowns.put(cooldownKey, System.currentTimeMillis() + (command.getCooldown() * 1000L));
        }

        // Execute command
        try {
            if (command.isAsync()) {
                // Execute asynchronously
                new Thread(() -> {
                    try {
                        invokeMethod(command.getHandler(), command.getMethod(), context);
                    } catch (Exception e) {
                        sender.sendError("An error occurred while executing the command.");
                        e.printStackTrace();
                    }
                }).start();
            } else {
                invokeMethod(command.getHandler(), command.getMethod(), context);
            }
            return CommandResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return CommandResult.failure("An error occurred while executing the command.");
        }
    }

    private CommandResult executeSubcommand(CommandSender sender, RegisteredCommand parent,
                                            RegisteredSubcommand subcommand, String[] args) {
        // Similar logic to executeCommand but for subcommands
        String cooldownKey = sender.getName() + ":" + parent.getName() + ":" + subcommand.getName();

        if (cooldowns.containsKey(cooldownKey)) {
            long timeLeft = cooldowns.get(cooldownKey) - System.currentTimeMillis();
            if (timeLeft > 0) {
                return CommandResult.failure("Subcommand on cooldown. Wait " + (timeLeft / 1000) + " seconds.");
            }
        }

        CommandContext context = new CommandContext(sender, parent.getName() + " " + subcommand.getName(), args);
        try {
            parseArguments(subcommand.getMethod(), context, args);
        } catch (ArgumentParseException e) {
            return CommandResult.failure("Invalid argument: " + e.getMessage());
        }

        if (subcommand.getCooldown() > 0) {
            cooldowns.put(cooldownKey, System.currentTimeMillis() + (subcommand.getCooldown() * 1000L));
        }

        try {
            if (subcommand.isAsync()) {
                new Thread(() -> {
                    try {
                        invokeMethod(subcommand.getHandler(), subcommand.getMethod(), context);
                    } catch (Exception e) {
                        sender.sendError("An error occurred while executing the subcommand.");
                        e.printStackTrace();
                    }
                }).start();
            } else {
                invokeMethod(subcommand.getHandler(), subcommand.getMethod(), context);
            }
            return CommandResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return CommandResult.failure("An error occurred while executing the subcommand.");
        }
    }

    private void parseArguments(Method method, CommandContext context, String[] args) throws ArgumentParseException {
        Parameter[] parameters = method.getParameters();
        int argIndex = 0;

        for (Parameter parameter : parameters) {
            if (parameter.getType() == CommandContext.class) {
                continue;
            }

            if (parameter.isAnnotationPresent(Flag.class)) {
                Flag flag = parameter.getAnnotation(Flag.class);
                boolean flagPresent = Arrays.stream(args).anyMatch(arg ->
                        arg.equals("--" + flag.name()) ||
                                Arrays.stream(flag.aliases()).anyMatch(alias -> arg.equals("-" + alias))
                );
                context.setFlag(flag.name(), flagPresent);
                continue;
            }

            if (parameter.isAnnotationPresent(Remaining.class)) {
                Remaining remaining = parameter.getAnnotation(Remaining.class);
                String[] remainingArgs = Arrays.copyOfRange(args, argIndex, args.length);
                context.setArg(remaining.name(), remainingArgs);
                break;
            }

            Arg arg = parameter.getAnnotation(Arg.class);
            String argName = arg != null && !arg.name().isEmpty() ? arg.name() : parameter.getName();

            if (argIndex >= args.length) {
                if (arg != null && arg.optional()) {
                    if (!arg.defaultValue().isEmpty()) {
                        context.setArg(argName, parseArgument(parameter.getType(), arg.defaultValue(), context.getSender()));
                    }
                    continue;
                } else {
                    throw new ArgumentParseException("Missing required argument: " + argName);
                }
            }

            Object parsedValue = parseArgument(parameter.getType(), args[argIndex], context.getSender());
            context.setArg(argName, parsedValue);
            argIndex++;
        }
    }

    private Object parseArgument(Class<?> type, String input, CommandSender sender) throws ArgumentParseException {
        ArgumentType<?> argumentType = argumentTypes.get(type.getName());
        if (argumentType == null) {
            throw new ArgumentParseException("Unsupported argument type: " + type.getSimpleName());
        }
        return argumentType.parse(sender, input);
    }

    private void invokeMethod(Object handler, Method method, CommandContext context) throws Exception {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];

            if (param.getType() == CommandContext.class) {
                args[i] = context;
            } else if (param.isAnnotationPresent(Arg.class)) {
                Arg arg = param.getAnnotation(Arg.class);
                String argName = arg.name().isEmpty() ? param.getName() : arg.name();
                args[i] = context.getArg(argName);
            } else if (param.isAnnotationPresent(Flag.class)) {
                Flag flag = param.getAnnotation(Flag.class);
                args[i] = context.hasFlag(flag.name());
            } else if (param.isAnnotationPresent(Remaining.class)) {
                Remaining remaining = param.getAnnotation(Remaining.class);
                args[i] = context.getArg(remaining.name());
            }
        }

        method.invoke(handler, args);
    }

    public List<String> tabComplete(CommandSender sender, String commandName, String[] args) {
        RegisteredCommand command = getCommand(commandName);
        if (command == null) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            completions.addAll(command.getSubcommandNames());
            if (command.getTabCompleter() != null) {
                completions.addAll(command.getTabCompleter().complete(sender, args));
            }
            return completions.stream()
                    .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                    .sorted()
                    .toList();
        }

        if (args.length > 1 && command.hasSubcommand(args[0])) {
            RegisteredSubcommand subcommand = command.getSubcommand(args[0]);
            String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
            return tabCompleteMethod(subcommand.getMethod(), sender, subArgs);
        }

        return tabCompleteMethod(command.getMethod(), sender, args);
    }

    private List<String> tabCompleteMethod(Method method, CommandSender sender, String[] args) {
        Parameter[] parameters = method.getParameters();
        int argIndex = args.length - 1;
        int paramIndex = 0;

        for (Parameter param : parameters) {
            if (param.getType() == CommandContext.class) {
                continue;
            }

            if (paramIndex == argIndex) {
                if (param.isAnnotationPresent(Arg.class)) {
                    Arg arg = param.getAnnotation(Arg.class);
                    if (arg.suggestions().length > 0) {
                        return Arrays.stream(arg.suggestions())
                                .filter(s -> s.toLowerCase().startsWith(args[argIndex].toLowerCase()))
                                .toList();
                    }

                    ArgumentType<?> argumentType = argumentTypes.get(param.getType().getName());
                    if (argumentType != null) {
                        return argumentType.tabComplete(sender, args[argIndex]);
                    }
                }
                break;
            }
            paramIndex++;
        }

        return Collections.emptyList();
    }

    private RegisteredCommand getCommand(String name) {
        RegisteredCommand command = commands.get(name);
        if (command != null) {
            return command;
        }

        String aliasTarget = aliases.get(name);
        return aliasTarget != null ? commands.get(aliasTarget) : null;
    }
}