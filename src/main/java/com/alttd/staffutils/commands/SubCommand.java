package com.alttd.staffutils.commands;

import com.alttd.staffutils.config.Config;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class SubCommand {

    public SubCommand() {}

    public abstract boolean onCommand(CommandSender commandSender, String[] args);

    public abstract String getName();

    public String getPermission() {
        return Config.PERMISSIONS.BASE + getName();
    }

    public abstract List<String> getTabComplete(CommandSender commandSender, String[] args);

    public abstract String getHelpMessage();
}
