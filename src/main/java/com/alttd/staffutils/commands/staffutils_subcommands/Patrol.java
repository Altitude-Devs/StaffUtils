package com.alttd.staffutils.commands.staffutils_subcommands;

import com.alttd.staffutils.commands.SubCommand;
import com.alttd.staffutils.config.Config;
import com.alttd.staffutils.config.Messages;
import com.alttd.staffutils.tasks.SaveStaffPatrolActions;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Patrol extends SubCommand {
    Object2ObjectArrayMap<UUID, Instant> patrolMap = new Object2ObjectArrayMap<>();
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final SaveStaffPatrolActions patrolActions;

    public Patrol(SaveStaffPatrolActions patrolActions) {
        this.patrolActions = patrolActions;
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
        Optional<? extends Player> optionalPatrolPlayer = player.getServer().getOnlinePlayers().stream()
                .filter(onlinePlayer -> onlinePlayer.getName().equalsIgnoreCase(playerName))
                .findFirst();
        if (optionalPatrolPlayer.isEmpty()) {
            player.sendMiniMessage(Messages.GENERIC.PLAYER_NOT_ONLINE, Placeholder.parsed("player", playerName));
            return;
        }

        Player patrolPlayer = optionalPatrolPlayer.get();

        if (patrolPlayer.hasPermission(getPermission() + ".bypass") || patrolPlayer.getUniqueId().equals(player.getUniqueId())) {
            player.sendMiniMessage(Messages.PATROL.CAN_NOT_PATROL_THIS_PLAYER, Placeholder.component("player", patrolPlayer.displayName()));
            return;
        }

        Instant lastPatrol = patrolMap.getOrDefault(patrolPlayer.getUniqueId(), null);

        player.teleport(patrolPlayer);
        player.sendMiniMessage(Messages.PATROL.PATROLLING_PLAYER, TagResolver.resolver(
                Placeholder.component("player", patrolPlayer.displayName()),
                Placeholder.parsed("last_patrol", (lastPatrol == null ? "previous reboot" : Duration.between(lastPatrol, Instant.now()).toMinutes() + " minutes ago"))
        ));
        patrolActions.addPatrolAction(player.getUniqueId());
    }

    private void patrolNextPlayer(Player player) {
        List<Player> onlinePlayers = player.getServer().getOnlinePlayers().stream()
                .filter(offlinePlayer -> !offlinePlayer.hasPermission(getPermission() + ".bypass"))
                .filter(offlinePlayer -> !offlinePlayer.getUniqueId().equals(player.getUniqueId()))
                .map(OfflinePlayer::getPlayer)
                .toList();

        if (onlinePlayers.isEmpty()) {
            player.sendMiniMessage(Messages.PATROL.NO_PLAYERS_TO_PATROL, null);
            return;
        }

        Optional<Player> optionalPlayer = onlinePlayers.stream().filter(mapPlayer -> patrolMap.containsKey(mapPlayer.getUniqueId())).findFirst();
        if (optionalPlayer.isEmpty()) {
            optionalPlayer = onlinePlayers.stream().min(Comparator.comparing(mapPlayer -> patrolMap.get(mapPlayer.getUniqueId())));
        }

        Player patrolPlayer = optionalPlayer.get();
        Instant lastPatrol = patrolMap.getOrDefault(patrolPlayer.getUniqueId(), null);
        patrolMap.put(patrolPlayer.getUniqueId(), Instant.now());

        player.teleport(patrolPlayer);
        player.sendMiniMessage(Messages.PATROL.PATROLLING_PLAYER, TagResolver.resolver(
                Placeholder.component("player", patrolPlayer.displayName()),
                Placeholder.parsed("last_patrol", (lastPatrol == null ? "previous reboot" : Duration.between(lastPatrol, Instant.now()).toMinutes() + " minutes ago"))
        ));
        patrolActions.addPatrolAction(player.getUniqueId());
    }

    private void listPlayersToPatrol(CommandSender commandSender) {
        Instant currentTime = Instant.now();

        List<Component> playerPartList = commandSender.getServer().getOnlinePlayers().stream()
                .filter(offlinePlayer -> !offlinePlayer.hasPermission(getPermission() + ".bypass"))
                .filter(player -> {
                    UUID playerUUID = player.getUniqueId();
                    Instant lastCheckedTime = patrolMap.getOrDefault(playerUUID, Instant.MIN);
                    Duration timeSinceLastChecked = Duration.between(lastCheckedTime, currentTime);
                    return timeSinceLastChecked.compareTo(Config.PATROL.MAX_UN_PATROLLED_DURATION) > 0;
                })
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
        return "patrol";
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
