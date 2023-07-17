package com.alttd.staffutils.auto_tasks;

import com.alttd.staffutils.config.Config;
import com.alttd.staffutils.config.Messages;
import com.alttd.staffutils.util.PlayerPatrolService;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
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

        String patrolPermission = Config.PERMISSIONS.BASE + Config.COMMAND_NAME.PATROL;
        int playersToPatrol = playerPatrolService.getPlayersToPatrol(Config.PATROL.MAX_UN_PATROLLED_DURATION, patrolPermission+ ".bypass").size();
        int onlinePlayers = Bukkit.getOnlinePlayers().size();

        if (Config.PATROL.UN_PATROLLED_PERCENT != 0 && (((double) playersToPatrol / onlinePlayers) * 100) < Config.PATROL.UN_PATROLLED_PERCENT) {
            return;
        }

        Bukkit.broadcast(MiniMessage.miniMessage().deserialize(Messages.PATROL.PATROL_REMINDER, TagResolver.resolver(
                Placeholder.parsed("amount", String.valueOf(playersToPatrol)),
                Placeholder.parsed("time", String.valueOf(Config.PATROL.MAX_UN_PATROLLED_DURATION.toMinutes()))
        )), patrolPermission);
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
