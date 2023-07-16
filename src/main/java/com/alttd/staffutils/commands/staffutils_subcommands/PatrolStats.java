package com.alttd.staffutils.commands.staffutils_subcommands;

import com.alttd.staffutils.commands.SubCommand;
import com.alttd.staffutils.config.Config;
import com.alttd.staffutils.config.Messages;
import org.bukkit.command.CommandSender;

import java.util.List;

public class PatrolStats extends SubCommand {
    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        return false;
    }

    @Override
    public String getName() {
        return Config.COMMAND_NAME.PATROL_STATS;
    }

    @Override
    public List<String> getTabComplete(CommandSender commandSender, String[] args) {
        return List.of();
    }

    @Override
    public String getHelpMessage() {
        return Messages.HELP.PATROL_STATS;
    }
}
