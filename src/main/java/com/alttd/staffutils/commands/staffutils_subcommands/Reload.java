package com.alttd.staffutils.commands.staffutils_subcommands;

import com.alttd.staffutils.StaffUtils;
import com.alttd.staffutils.commands.SubCommand;
import com.alttd.staffutils.config.Messages;
import org.bukkit.command.CommandSender;

import java.util.List;

public class Reload extends SubCommand {

    private final StaffUtils staffUtils;

    public Reload(StaffUtils staffUtils) {
        this.staffUtils = staffUtils;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        staffUtils.reloadConfigs();
        commandSender.sendMiniMessage(Messages.RELOAD.RELOADED, null);
        return true;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public List<String> getTabComplete(CommandSender commandSender, String[] args) {
        return List.of();
    }

    @Override
    public String getHelpMessage() {
        return Messages.HELP.RELOAD;
    }
}
