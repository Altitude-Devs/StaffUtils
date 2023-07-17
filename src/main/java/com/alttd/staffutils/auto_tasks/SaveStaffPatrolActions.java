package com.alttd.staffutils.auto_tasks;

import com.alttd.staffutils.config.Config;
import com.alttd.staffutils.database.DatabaseManager;
import com.alttd.staffutils.util.Logger;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class SaveStaffPatrolActions extends Task {

    Object2IntArrayMap<UUID> patrolActions = new Object2IntArrayMap<>();
    private Instant lastExecution;
    private final Logger logger;

    public SaveStaffPatrolActions(Logger logger) {
        this.logger = logger;
        lastExecution = Instant.MIN;
    }

    public synchronized void addPatrolAction(UUID uuid) {
        int currentActions = patrolActions.getOrDefault(uuid, 0);
        patrolActions.put(uuid, currentActions + 1);
    }

    public synchronized void save() {
        Connection connection = DatabaseManager.getConnection();
        String sql = "INSERT INTO patrol_stats (uuid, time_millis, patrol_amount) VALUES (?, ?, ?)";
        long time = System.currentTimeMillis();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Map.Entry<UUID, Integer> entry : patrolActions.object2IntEntrySet()) {
                UUID uuid = entry.getKey();
                int patrolAmount = entry.getValue();

                statement.setString(1, uuid.toString());
                statement.setLong(2, time);
                statement.setInt(3, patrolAmount);
                statement.addBatch();
            }
            statement.executeBatch();
            patrolActions.clear();
        } catch (SQLException sqlException) {
            logger.severe("Encountered sql exception when saving patrol stats");
            sqlException.printStackTrace();
        }
    }

    @Override
    void execute() {
        lastExecution = Instant.now();
        if (patrolActions.isEmpty())
            return;
        save();
    }

    @Override
    Duration getFrequency() {
        return Config.PATROL.SAVE_STATS_FREQUENCY;
    }

    @Override
    Instant getLastExecution() {
        return lastExecution;
    }

    @Override
    boolean shouldExecuteOnShutdown() {
        return true;
    }
}
