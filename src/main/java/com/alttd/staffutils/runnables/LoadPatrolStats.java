package com.alttd.staffutils.runnables;

import com.alttd.staffutils.config.Messages;
import com.alttd.staffutils.database.DatabaseManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

public class LoadPatrolStats extends BukkitRunnable {

    private final CommandSender commandSender;
    private final OfflinePlayer offlinePlayer;
    private final Duration duration;
    private final boolean allPlayers;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public LoadPatrolStats(@NotNull CommandSender commandSender, @NotNull Duration duration, @NotNull OfflinePlayer offlinePlayer) {
        this.commandSender = commandSender;
        this.offlinePlayer = offlinePlayer;
        this.duration = duration;
        this.allPlayers = false;
    }

    public LoadPatrolStats(@NotNull CommandSender commandSender, @NotNull Duration duration) {
        this.commandSender = commandSender;
        this.offlinePlayer = null;
        this.duration = duration;
        this.allPlayers = true;
    }

    @Override
    public void run() {
        String sql = "SELECT uuid,time_millis,SUM(patrol_amount) AS patrol_amount FROM patrol_stats WHERE patrol_stats.time_millis >= ? GROUP BY uuid";
        if (!allPlayers)
            sql += " AND uuid = ?";

        Instant startTime = Instant.now().minus(duration);
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, startTime.toEpochMilli());
            if (!allPlayers)
                statement.setString(2, offlinePlayer.getUniqueId().toString());

            ResultSet resultSet = statement.executeQuery();
            ArrayList<Component> components = new ArrayList<>();
            while (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                int patrolAmount = resultSet.getInt("patrol_amount");
                OfflinePlayer patrolStatsPlayer = Bukkit.getServer().getOfflinePlayer(uuid);

                TagResolver.Single playerTag;
                if (patrolStatsPlayer.getName() == null)
                    playerTag = Placeholder.parsed("player", uuid.toString());
                else
                    playerTag = Placeholder.parsed("player", patrolStatsPlayer.getName());

                components.add(miniMessage.deserialize(Messages.PATROL_STATS.PART, TagResolver.resolver(
                        playerTag, Placeholder.parsed("amount", String.valueOf(patrolAmount))
                )));
            }

            Component join = Component.join(JoinConfiguration.newlines(), components);
            commandSender.sendMiniMessage(Messages.PATROL_STATS.MESSAGE, TagResolver.resolver(
                    Placeholder.component("players", join),
                    Placeholder.parsed("days", String.valueOf(duration.toDays()))
            ));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
