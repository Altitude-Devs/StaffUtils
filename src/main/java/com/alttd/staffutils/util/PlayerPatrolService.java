package com.alttd.staffutils.util;

import com.alttd.staffutils.config.Config;
import com.alttd.staffutils.tasks.SaveStaffPatrolActions;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerPatrolService {

    private final Object2ObjectArrayMap<UUID, Instant> patrolMap = new Object2ObjectArrayMap<>();
    private final SaveStaffPatrolActions patrolActions;

    public PlayerPatrolService(SaveStaffPatrolActions patrolActions) {
        this.patrolActions = patrolActions;
    }

    public synchronized boolean patrolPlayer(Player patroller, Player toPatrol) {
        boolean teleport = patroller.teleport(toPatrol);
        if (teleport) {
            patrolMap.put(toPatrol.getUniqueId(), Instant.now());
            patrolActions.addPatrolAction(patroller.getUniqueId());
        }
        return teleport;
    }

    public synchronized Instant getLastPatrolled(Player player) {
        return patrolMap.getOrDefault(player.getUniqueId(), Instant.MIN);
    }

    public synchronized Optional<Player> getPlayerToPatrol(@NotNull String username, @NotNull String bypassPermission) {
        return Bukkit.getOnlinePlayers().stream()
                .filter(onlinePlayer -> onlinePlayer.getName().equalsIgnoreCase(username))
                .filter(onlinePlayer -> !onlinePlayer.hasPermission(bypassPermission))
                .map(onlinePlayer -> (Player) onlinePlayer)
                .findFirst();
    }

    public synchronized Optional<Player> getNextPlayer(@NotNull String bypassPermission) {
        return getPlayersToPatrol(Config.PATROL.MAX_UN_PATROLLED_DURATION, bypassPermission).stream()
                .min(Comparator.comparing(mapPlayer -> patrolMap.getOrDefault(mapPlayer.getUniqueId(), Instant.MIN)));
    }

    public List<Player> getPlayersToPatrol(@NotNull Duration lastPatrolled, @Nullable String bypassPermission) {
        Instant currentTime = Instant.now();
        Stream<? extends Player> playerStream;
        if (bypassPermission == null || bypassPermission.isBlank())
            playerStream = Bukkit.getServer().getOnlinePlayers().stream();
        else
            playerStream = Bukkit.getServer().getOnlinePlayers().stream()
                .filter(onlinePlayer -> !onlinePlayer.hasPermission(bypassPermission));

        if (!lastPatrolled.isZero()) {
            playerStream = playerStream.filter(player -> {
                UUID playerUUID = player.getUniqueId();
                Instant lastCheckedTime = patrolMap.getOrDefault(playerUUID, Instant.MIN);
                Duration timeSinceLastChecked = Duration.between(lastCheckedTime, currentTime);
                return timeSinceLastChecked.compareTo(lastPatrolled) > 0;
            });
        }

        return playerStream.collect(Collectors.toList());
    }

}
