package com.alttd.staffutils.commands;

import com.alttd.staffutils.StaffUtils;
import com.alttd.staffutils.commands.staffutils_subcommands.*;
import com.alttd.staffutils.config.Messages;
import com.alttd.staffutils.util.Logger;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StaffUtilCommand implements CommandExecutor, TabExecutor {
    private final List<SubCommand> subCommands;

    public StaffUtilCommand(StaffUtils staffUtils, Logger logger) {
        PluginCommand command = staffUtils.getCommand("staffutils");
        if (command == null) {
            subCommands = null;
            logger.severe("Unable to find staffutils command.");
            return;
        }
        command.setExecutor(this);
        command.setTabCompleter(this);
        command.setAliases(List.of("su"));

        subCommands = Arrays.asList(
                new Reload(staffUtils),
                new Top(),
                new ItemName(),
                new ItemLore(),
                new Ride(),
                new Uppies(),
                new Shake(),
                new Sudo()
        );
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String cmd, @NotNull String[] args) {
        if (args.length == 0) {
            commandSender.sendMiniMessage(Messages.HELP.HELP_MESSAGE_WRAPPER.replaceAll("<commands>", subCommands.stream()
                    .filter(subCommand -> commandSender.hasPermission(subCommand.getPermission()))
                    .map(SubCommand::getHelpMessage)
                    .collect(Collectors.joining("\n"))), null);
            return true;
        }

        Optional<SubCommand> optionalSubCommand = getSubCommand(args[0]);
        if (optionalSubCommand.isEmpty())
            return false;

        SubCommand subCommand = optionalSubCommand.get();
        if (!commandSender.hasPermission(subCommand.getPermission())) {
            commandSender.sendMiniMessage(Messages.GENERIC.NO_PERMISSION, null);
            return true;
        }

        boolean executedCorrectly = subCommand.onCommand(commandSender, args);
        if (!executedCorrectly) {
            commandSender.sendMiniMessage(subCommand.getHelpMessage(), null);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String cmd, @NotNull String[] args) {
        List<String> res = new ArrayList<>();

        if (args.length <= 1) {
            res.addAll(subCommands.stream()
                    .filter(subCommand -> commandSender.hasPermission(subCommand.getPermission()))
                    .map(SubCommand::getName)
                    .filter(name -> args.length == 0 || name.startsWith(args[0]))
                    .toList()
            );
        } else {
            Optional<SubCommand> optionalSubCommand = getSubCommand(args[0]);
            if (optionalSubCommand.isEmpty())
                return List.of();
            SubCommand subCommand = optionalSubCommand.get();
            if (commandSender.hasPermission(subCommand.getPermission()))
                res.addAll(subCommand.getTabComplete(commandSender, args).stream()
                        .filter(str -> str.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                        .toList());
        }
        return res;
    }

    public List<SubCommand> getSubCommands() {
        return subCommands;
    }

    public Optional<SubCommand> getSubCommand(String cmdName) {
        return subCommands.stream()
                .filter(subCommand -> subCommand.getName().equals(cmdName))
                .findFirst();
    }
}