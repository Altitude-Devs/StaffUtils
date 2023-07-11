package com.alttd.staffutils.commands.staffutils_subcommands;

import com.alttd.staffutils.commands.SubCommand;
import com.alttd.staffutils.config.Messages;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class Uppies extends SubCommand {
    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMiniMessage(Messages.GENERIC.PLAYER_ONLY, null);
            return true;
        }

        Entity mountingTarget = player;
        while (mountingTarget.getPassengers().size() != 0) {
            mountingTarget = mountingTarget.getPassengers().get(0);
        }
//        if (player.getPassengers().size() != 0) {
//            commandSender.sendMiniMessage(Messages.UPPIES.ALREADY_HAVE_PASSENGER, Placeholder.component("name", player.getPassengers().get(0).name()));
//            return true;
//        }

        Entity targetEntity = player.getTargetEntity(10);
        if (targetEntity == null) {
            commandSender.sendMiniMessage(Messages.UPPIES.NO_TARGET, null);
            return true;
        }

        if (!mountingTarget.addPassenger(targetEntity)) {
            commandSender.sendMiniMessage(Messages.UPPIES.UNABLE_TO_ADD_PASSENGER,  Placeholder.component("name", targetEntity.name()));
            return true;
        }

        commandSender.sendMiniMessage(Messages.UPPIES.NOW_CARRYING, Placeholder.component("name", targetEntity.name()));
        if (targetEntity instanceof Player targetPlayer) {
            targetPlayer.sendMiniMessage(Messages.UPPIES.NOW_RIDING, Placeholder.component("name", player.name()));
        }
        return true;
    }

    @Override
    public String getName() {
        return "uppies";
    }

    @Override
    public List<String> getTabComplete(CommandSender commandSender, String[] args) {
        return List.of();
    }

    @Override
    public String getHelpMessage() {
        return Messages.HELP.UPPIES;
    }
}
