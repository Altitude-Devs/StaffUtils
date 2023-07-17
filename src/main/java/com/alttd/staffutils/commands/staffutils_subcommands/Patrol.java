package com.alttd.staffutils.commands.staffutils_subcommands;

import com.alttd.staffutils.commands.SubCommand;
import com.alttd.staffutils.config.Config;
import com.alttd.staffutils.config.Messages;
import com.alttd.staffutils.util.PlayerPatrolService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Patrol extends SubCommand {
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final PlayerPatrolService playerPatrolService;

    public Patrol(PlayerPatrolService playerPatrolService) {
        this.playerPatrolService = playerPatrolService;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        if (args.length == 2 && args[1].equalsIgnoreCase("list")) {
            listPlayersToPatrol(commandSender);
            return true;
        }

        if (!(commandSender instanceof Player player)) {
            commandSender.sendMiniMessage(Messages.GENERIC.PLAYER_ONLY, null);
            return true;
        }

        if (args.length == 2) {
            patrolPlayer(player, args[1]);
        } else {
            patrolNextPlayer(player);
        }

        return true;
    }

    private void patrolPlayer(Player player, String playerName) {
        Optional<Player> optionalPatrolPlayer = playerPatrolService.getPlayerToPatrol(playerName, getPermission() + ".bypass");
        if (optionalPatrolPlayer.isEmpty()) {
            player.sendMiniMessage(Messages.PATROL.FAILED_TO_PATROL_PLAYER, Placeholder.parsed("player", playerName));
            return;
        }

        Player patrolPlayer = optionalPatrolPlayer.get();
        if (patrolPlayer.getUniqueId().equals(player.getUniqueId())) {
            player.sendMiniMessage(Messages.PATROL.CAN_NOT_PATROL_SELF, Placeholder.component("player", patrolPlayer.displayName()));
            return;
        }

        patrolToPlayer(player, patrolPlayer);
    }

    private void patrolToPlayer(Player player, Player patrolPlayer) {
        Instant lastPatrol = playerPatrolService.getLastPatrolled(patrolPlayer);
        if (playerPatrolService.patrolPlayer(player, patrolPlayer)) {
            player.sendMiniMessage(Messages.PATROL.PATROLLING_PLAYER, TagResolver.resolver(
                    Placeholder.component("player", patrolPlayer.displayName()),
                    Placeholder.parsed("last_patrol", (lastPatrol.equals(Instant.MIN) ? "previous reboot" : Duration.between(lastPatrol, Instant.now()).toMinutes() + " minutes ago"))
            ));
        } else {
            player.sendMiniMessage(Messages.PATROL.FAILED_TO_PATROL_PLAYER, Placeholder.component("player", patrolPlayer.displayName()));
        }
    }

    private void patrolNextPlayer(Player player) {
        Optional<Player> nextPlayer = playerPatrolService.getNextPlayer(getPermission() + ".bypass");

        if (nextPlayer.isEmpty()) {
            player.sendMiniMessage(Messages.PATROL.NO_PLAYERS_TO_PATROL, null);
            return;
        }

        Player patrolPlayer = nextPlayer.get();
        patrolToPlayer(player, patrolPlayer);
    }

    private void listPlayersToPatrol(CommandSender commandSender) {
        List<Component> playerPartList = playerPatrolService.getPlayersToPatrol(Config.PATROL.MAX_UN_PATROLLED_DURATION, getPermission() + ".bypass").stream()
                .map(player -> miniMessage.deserialize(Messages.PATROL.PLAYER_PART, Placeholder.component("player", player.displayName())))
                .collect(Collectors.toList());

        Component playersPart = Component.join(JoinConfiguration.separator(miniMessage.deserialize(Messages.PATROL.PLAYER_PART_SEPARATOR)), playerPartList);

        commandSender.sendMiniMessage(Messages.PATROL.LIST_PLAYERS_TO_PATROL, TagResolver.resolver(
                Placeholder.parsed("minutes", String.valueOf(Config.PATROL.MAX_UN_PATROLLED_DURATION.toMinutes())),
                Placeholder.component("players", playersPart)
        ));
    }

    @Override
    public String getName() {
        return Config.COMMAND_NAME.PATROL;
    }

    @Override
    public List<String> getTabComplete(CommandSender commandSender, String[] args) {
        if (args.length == 2)
            return List.of("list");
        return List.of();
    }

    @Override
    public String getHelpMessage() {
        return Messages.HELP.PATROL;
    }
}
