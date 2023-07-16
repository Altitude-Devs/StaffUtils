package com.alttd.staffutils.commands.staffutils_subcommands;

import com.alttd.staffutils.commands.SubCommand;
import com.alttd.staffutils.config.Config;
import com.alttd.staffutils.config.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class Shake extends SubCommand {
    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMiniMessage(Messages.GENERIC.PLAYER_ONLY, null);
            return true;
        }

        List<Entity> passengers = player.getPassengers();
        if (passengers.size() == 0) {
            commandSender.sendMiniMessage(Messages.SHAKE.NO_PASSENGERS, null);
            return true;
        }

        recRemovesPassengers(player);
        player.sendMiniMessage(Messages.SHAKE.DONE, null);
        return false;
    }

    private void recRemovesPassengers(Entity entity) {
        entity.getPassengers().forEach(this::recRemovesPassengers);
        entity.getPassengers().forEach(entity::removePassenger);
    }

    @Override
    public String getName() {
        return Config.COMMAND_NAME.SHAKE;
    }

    @Override
    public List<String> getTabComplete(CommandSender commandSender, String[] args) {
        return List.of();
    }

    @Override
    public String getHelpMessage() {
        return Messages.HELP.SHAKE;
    }
}
