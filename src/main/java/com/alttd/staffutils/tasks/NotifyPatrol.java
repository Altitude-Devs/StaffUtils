package com.alttd.staffutils.tasks;

import com.alttd.staffutils.config.Config;
import com.alttd.staffutils.util.PlayerPatrolService;
import org.bukkit.Bukkit;

import java.time.Duration;
import java.time.Instant;

public class NotifyPatrol extends Task {

    private Instant lastExecution;
    private final PlayerPatrolService playerPatrolService;

    public NotifyPatrol(PlayerPatrolService playerPatrolService) {
        this.lastExecution = Instant.MIN;
        this.playerPatrolService = playerPatrolService;
    }

    @Override
    void execute() {
        lastExecution = Instant.now();
        int playersToPatrol = playerPatrolService.getPlayersToPatrol(Config.PATROL.MAX_UN_PATROLLED_DURATION, "staffutils.patrol.bypass").size();
        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        if (Config.PATROL.UN_PATROLLED_PERCENT != 0 && (((double) playersToPatrol / onlinePlayers) * 100) < Config.PATROL.UN_PATROLLED_PERCENT) {
            return;
        }

    }

    @Override
    Duration getFrequency() {
        return Config.PATROL.REMINDER_FREQUENCY;
    }

    @Override
    Instant getLastExecution() {
        return lastExecution;
    }

    @Override
    boolean shouldExecuteOnShutdown() {
        return false;
    }
}
