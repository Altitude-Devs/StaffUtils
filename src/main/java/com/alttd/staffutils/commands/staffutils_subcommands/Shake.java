package com.alttd.staffutils.commands.staffutils_subcommands;

import com.alttd.staffutils.commands.SubCommand;
import com.alttd.staffutils.config.Config;
import com.alttd.staffutils.config.Messages;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class Shake extends SubCommand {
    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        if (args.length == 2) {
            if (!commandSender.hasPermission(getPermission() + ".other")) {
                commandSender.sendMiniMessage(Messages.GENERIC.NO_PERMISSION, null);
                return true;
            }
            shakeOther(commandSender, args);
            return true;
        }
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMiniMessage(Messages.GENERIC.PLAYER_ONLY, null);
            return true;
        }

        if (!shakeFromPlayer(player, commandSender))
            return true;

        player.sendMiniMessage(Messages.SHAKE.DONE, null);
        return true;
    }

    private boolean shakeFromPlayer(Player player, CommandSender commandSender) {
        List<Entity> passengers = player.getPassengers();
        if (passengers.size() == 0) {
            commandSender.sendMiniMessage(Messages.SHAKE.NO_PASSENGERS, null);
            return false;
        }

        recRemovesPassengers(player);
        return true;
    }

    private void shakeOther(CommandSender commandSender, String[] args) {
        Player player = commandSender.getServer().getPlayer(args[1]);
        if (player == null) {
            commandSender.sendMiniMessage(Messages.GENERIC.PLAYER_NOT_ONLINE, Placeholder.parsed("player", args[1]));
            return;
        }

        shakeFromPlayer(player, commandSender);
        commandSender.sendMiniMessage(Messages.SHAKE.DONE_OTHER, Placeholder.component("player", player.name()));
        player.sendMiniMessage(Messages.SHAKE.DONE_BY_OTHER,  Placeholder.parsed("player", commandSender.getName()));
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
