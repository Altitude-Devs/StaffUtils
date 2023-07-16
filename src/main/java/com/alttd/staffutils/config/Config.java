package com.alttd.staffutils.config;

import com.alttd.staffutils.util.Logger;

import java.io.File;
import java.time.Duration;

public class Config extends AbstractConfig {

    static Config config;
    private Logger logger;

    Config(Logger logger) {
        super(
                new File(System.getProperty("user.home") + File.separator
                        + "share" + File.separator
                        + "configs" + File.separator
                        + "StaffUtils"),
                "config.yml", logger);
        this.logger = logger;
    }

    public static void reload(Logger logger) {
        logger.info("Reloading config");
        config = new Config(logger);
        config.readConfig(Config.class, null);
    }

    public static class LOGGING {
        private static final String prefix = "logging.";
        public static boolean DEBUG = false;
        public static boolean WARNINGS = true;

        @SuppressWarnings("unused")
        private static void load() {
            DEBUG = config.getBoolean(prefix, "debug", DEBUG);
            WARNINGS = config.getBoolean(prefix, "warnings", WARNINGS);
        }
    }

    public static class TASKS {
        private static final String prefix = "tasks.";
        public static int CHECK_EXECUTE_FREQUENCY_MINUTES = 1;

        @SuppressWarnings("unused")
        private static void load() {
            CHECK_EXECUTE_FREQUENCY_MINUTES = config.getInt(prefix, "check-execute-frequency-minutes", CHECK_EXECUTE_FREQUENCY_MINUTES);
        }
    }

    public static class DATABASE {
        private static final String prefix = "database.";

        public static String DRIVER = "mysql";
        public static String IP = "0.0.0.0";
        public static String PORT = "3306";
        public static String DATABASE = "staff_utils";
        public static String USERNAME = "root";
        public static String PASSWORD = "root";

        @SuppressWarnings("unused")
        private static void load() {
            DRIVER = config.getString(prefix, "driver", DRIVER);
            IP = config.getString(prefix, "ip", IP);
            PORT = config.getString(prefix, "port", PORT);
            DATABASE = config.getString(prefix, "database", DATABASE);
            USERNAME = config.getString(prefix, "username", USERNAME);
            PASSWORD = config.getString(prefix, "password", PASSWORD);
        }
    }

    public static class PATROL {
        private static final String prefix = "patrol.";
        public static Duration MAX_UN_PATROLLED_DURATION = Duration.ofMinutes(30);
        public static Duration REMINDER_FREQUENCY = Duration.ofMinutes(10);
        public static int UN_PATROLLED_PERCENT = 50;

        public static Duration SAVE_STATS_FREQUENCY = Duration.ofMinutes(5);

        @SuppressWarnings("unused")
        private static void load() {
            MAX_UN_PATROLLED_DURATION = Duration.ofMinutes(config.getLong(prefix, "max-un-patrolled-time-minutes", MAX_UN_PATROLLED_DURATION.toMinutes()));
            REMINDER_FREQUENCY = Duration.ofMinutes(config.getLong(prefix, "reminder-frequency-minutes", REMINDER_FREQUENCY.toMinutes()));
            UN_PATROLLED_PERCENT = config.getInt(prefix, "un-patrolled-percent", UN_PATROLLED_PERCENT);
            SAVE_STATS_FREQUENCY = Duration.ofMinutes(config.getLong(prefix, "save-stats-frequency-minutes", SAVE_STATS_FREQUENCY.toMinutes()));
        }

    }

    public static class PERMISSIONS {
        private static final String prefix = "permissions.";

        public static String BASE = "staffutils.";

        @SuppressWarnings("unused")
        private static void load() {
            BASE = config.getString(prefix, "base", BASE);
        }
    }

    public static class COMMAND_NAME {
        private static final String prefix = "command-name.";

        public static String ITEM_LORE = "itemlore";
        public static String ITEM_NAME = "itemname";
        public static String PATROL = "patrol";
        public static String PATROL_STATS = "patrolstats";
        public static String RELOAD = "reload";
        public static String RIDE = "ride";
        public static String SHAKE = "shake";
        public static String SUDO = "sudo";
        public static String TOP = "top";
        public static String UPPIES = "uppies";

        @SuppressWarnings("unused")
        private static void load() {
            ITEM_LORE = config.getString(prefix, "item-lore", ITEM_LORE);
            ITEM_NAME = config.getString(prefix, "item-name", ITEM_NAME);
            PATROL = config.getString(prefix, "patrol", PATROL);
            PATROL_STATS = config.getString(prefix, "patrol-stats", PATROL_STATS);
            RELOAD = config.getString(prefix, "reload", RELOAD);
            RIDE = config.getString(prefix, "ride", RIDE);
            SHAKE = config.getString(prefix, "shake", SHAKE);
            SUDO = config.getString(prefix, "sudo", SUDO);
            TOP = config.getString(prefix, "top", TOP);
            UPPIES = config.getString(prefix, "uppies", UPPIES);
        }
    }
}
