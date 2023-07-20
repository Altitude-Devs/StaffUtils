package com.alttd.staffutils.commands.staffutils_subcommands;

import com.alttd.staffutils.StaffUtils;
import com.alttd.staffutils.commands.SubCommand;
import com.alttd.staffutils.config.Config;
import com.alttd.staffutils.config.Messages;
import com.alttd.staffutils.runnables.LoadPatrolStats;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.time.Duration;
import java.util.List;

public class PatrolStats extends SubCommand {

    private final StaffUtils staffUtils;

    public PatrolStats(StaffUtils staffUtils) {
        this.staffUtils = staffUtils;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1)
            new LoadPatrolStats(commandSender, Duration.ofDays(7)).runTaskAsynchronously(staffUtils);
        else if (args.length <= 3) {
            int days;
            try {
                days = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                return false;
            }
            if (args.length == 2) {
                new LoadPatrolStats(commandSender, Duration.ofDays(days)).runTaskAsynchronously(staffUtils);
            }
            else {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[2]);
                if (!offlinePlayer.hasPlayedBefore())
                    return false;
                new LoadPatrolStats(commandSender, Duration.ofDays(days), offlinePlayer).runTaskAsynchronously(staffUtils);
            }
        } else
            return false;
        return true;
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
