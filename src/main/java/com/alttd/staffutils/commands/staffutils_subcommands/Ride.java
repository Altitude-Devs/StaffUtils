package com.alttd.staffutils.commands.staffutils_subcommands;

import com.alttd.staffutils.commands.SubCommand;
import com.alttd.staffutils.config.Messages;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class Ride extends SubCommand {
    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMiniMessage(Messages.GENERIC.PLAYER_ONLY, null);
            return true;
        }

        Entity targetEntity = player.getTargetEntity(10);
        if (targetEntity == null) {
            commandSender.sendMiniMessage(Messages.RIDE.NO_TARGET, null);
            return true;
        }

        if (targetEntity.getPassengers().size() != 0) {
            commandSender.sendMiniMessage(Messages.RIDE.ALREADY_HAS_PASSENGER, Placeholder.component("name", targetEntity.name()));
            return true;
        }

        if (!targetEntity.addPassenger(player)) {
            commandSender.sendMiniMessage(Messages.RIDE.UNABLE_TO_ADD_PASSENGER, Placeholder.component("name", targetEntity.name()));
            return true;
        }

        commandSender.sendMiniMessage(Messages.RIDE.NOW_RIDING, Placeholder.component("name", targetEntity.name()));
        return true;
    }

    @Override
    public String getName() {
        return "ride";
    }

    @Override
    public List<String> getTabComplete(CommandSender commandSender, String[] args) {
        return List.of();
    }

    @Override
    public String getHelpMessage() {
        return Messages.HELP.RIDE;
    }
}
