package com.alttd.staffutils.database;

import com.alttd.staffutils.config.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static Connection connection = null;

    public static void initiate() {
        openConnection();
        createPatrolStatsTable();
    }

    private static void openConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }

            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            connection = DriverManager.getConnection(
                    "jdbc:" + Config.DATABASE.DRIVER + "://" + Config.DATABASE.IP + ":" + Config.DATABASE.PORT + "/" + Config.DATABASE.DATABASE + "?useSSL=false", Config.DATABASE.USERNAME,
                    Config.DATABASE.PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed())
                return connection;
            openConnection();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return connection;
    }

    private static void createPatrolStatsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS patrol_stats (" +
                "uuid VARCHAR(36) NOT NULL, " +
                "time_millis BIGINT(19) NOT NULL, " +
                "patrol_amount INT NOT NULL, " +
                "PRIMARY KEY(uuid, time_millis)" +
                ");";
        try {
            Statement statement = getConnection().createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
