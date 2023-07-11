package com.alttd.staffutils.commands.staffutils_subcommands;

import com.alttd.staffutils.commands.SubCommand;
import com.alttd.staffutils.config.Messages;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Sudo extends SubCommand {
    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        if (args.length <= 3)
            return false;
        if (args[2].equalsIgnoreCase("c:")) {
            sendText(commandSender, args);
            return true;
        }
        sendCommand(commandSender, args);
        return true;
    }

    private Optional<List<Player>> getPlayersToSudo(CommandSender commandSender, String[] args) {
        if (args[2].equals("*")) {
            return Optional.of(commandSender.getServer().getOnlinePlayers().stream()
                    .filter(player -> !player.hasPermission(getPermission() + ".prevent"))
                    .collect(Collectors.toList()));
        } else {
            Player player = commandSender.getServer().getPlayer(args[1]);
            if (player == null || !player.isOnline()) {
                commandSender.sendMiniMessage(Messages.SUDO.INVALID_PLAYER, Placeholder.parsed("player", args[1]));
                return Optional.empty();
            }
            if (player.hasPermission(getPermission() + ".prevent")) {
                commandSender.sendMiniMessage(Messages.SUDO.CAN_NOT_SUDO_THIS_PLAYER, Placeholder.parsed("player", args[1]));
                return Optional.empty();
            }
            return Optional.of(List.of(player));
        }
    }

    private void sendText(CommandSender commandSender, String[] args) {
        String sudoCommand = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
        String substring = sudoCommand.substring(3);
        Optional<List<Player>> playersToSudo = getPlayersToSudo(commandSender, args);
        if (playersToSudo.isEmpty()) {
            return;
        }
        playersToSudo.get().forEach(player -> player.sendMessage(substring));
        commandSender.sendMiniMessage(Messages.SUDO.SUCCESS, Placeholder.parsed("player", args[1]));
    }

    private void sendCommand(CommandSender commandSender, String[] args) {
        String substring = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
        Optional<List<Player>> playersToSudo = getPlayersToSudo(commandSender, args);
        if (playersToSudo.isEmpty())
            return;
        playersToSudo.get().forEach(player -> player.performCommand(substring));
        commandSender.sendMiniMessage(Messages.SUDO.SUCCESS, Placeholder.parsed("player", args[1]));
    }

    @Override
    public String getName() {
        return "sudo";
    }

    @Override
    public List<String> getTabComplete(CommandSender commandSender, String[] args) {
        if (args.length == 1 || args.length == 2) {
            return commandSender.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        }
        return List.of();
    }

    @Override
    public String getHelpMessage() {
        return null;
    }
}
