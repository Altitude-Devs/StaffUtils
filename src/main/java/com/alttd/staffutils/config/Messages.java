package com.alttd.staffutils.config;

import com.alttd.staffutils.util.Logger;

import java.io.File;

public class Messages extends AbstractConfig {
    static Messages config;
    private final Logger logger;

    Messages(Logger logger) {
        super(
                new File(System.getProperty("user.home") + File.separator
                        + "share" + File.separator
                        + "configs" + File.separator
                        + "StaffUtils"),
                "messages.yml", logger);
        this.logger = logger;
    }

    public static void reload(Logger logger) {
        config = new Messages(logger);
        config.readConfig(Messages.class, null);
    }

    public static class HELP {
        private static final String prefix = "help.";

        public static String HELP_MESSAGE_WRAPPER = "<gold>StaffUtils help:\n<commands></gold>";
        public static String HELP_MESSAGE = "<green>Show this menu: <gold>/su help</gold></green>";
        public static String RELOAD = "<green>Reload the configs for StaffUtils: <gold>/su reload</gold></green>";
        public static String TOP = "<green>Teleports you to the highest location above you: <gold>/su top</gold></green>";

        @SuppressWarnings("unused")
        private static void load() {
            HELP_MESSAGE_WRAPPER = config.getString(prefix, "help-wrapper", HELP_MESSAGE_WRAPPER);
            HELP_MESSAGE = config.getString(prefix, "help", HELP_MESSAGE);
            RELOAD = config.getString(prefix, "reload", RELOAD);
            TOP = config.getString(prefix, "top", TOP);
        }
    }

    public static class GENERIC {
        private static final String prefix = "generic.";

        public static String NO_PERMISSION = "<red>You don't have permission for this command</red>";
        public static String PLAYER_ONLY = "<red>This command can only be executed as a player</red>";

        @SuppressWarnings("unused")
        private static void load() {
            NO_PERMISSION = config.getString(prefix, "no-permission", NO_PERMISSION);
            PLAYER_ONLY = config.getString(prefix, "player-only", PLAYER_ONLY);
        }
    }

    public static class RELOAD {
        private static final String prefix = "su-command.reload.";

        public static String RELOADED = "<green>Reloaded configs</green>";

        @SuppressWarnings("unused")
        private static void load() {
            RELOADED = config.getString(prefix, "reloaded", RELOADED);
        }
    }

    public static class TOP {
        private static final String prefix = "su-command.top.";

        public static String TELEPORTED = "<green>Teleported you to <x> <y> <z></green>";

        @SuppressWarnings("unused")
        private static void load() {
            TELEPORTED = config.getString(prefix, "teleported", TELEPORTED);
        }
    }
}
