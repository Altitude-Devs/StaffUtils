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
        public static String ITEM_NAME = "<green>Renames an item with the name you provide: <gold>/su itemname <name></gold></green>";
        public static String ITEM_LORE = "<green>Modify lore on an item with the lore you provide: <gold>/su itemlore [line] <lore></gold></green>";
        public static String RIDE = "<green>Ride the entity you're looking at: <gold>/su ride</gold></green>";
        public static String UPPIES = "<green>Carry the entity you're looking at: <gold>/su uppies</gold></green>";
        public static String SHAKE = "<green>Shake of all your passengers: <gold>/su shake</gold></green>";
        public static String PATROL = "<green>Patrol players: <gold>/su patrol</gold></green>";
        public static String PATROL_STATS = "<green>See patrol stats of staff: <gold>/su patrolstats <player></gold></green>";
        public static String SUDO = "<green>Sudo a player: <gold>/su sudo <player> <command></gold></green>";

        @SuppressWarnings("unused")
        private static void load() {
            HELP_MESSAGE_WRAPPER = config.getString(prefix, "help-wrapper", HELP_MESSAGE_WRAPPER);
            HELP_MESSAGE = config.getString(prefix, "help", HELP_MESSAGE);
            RELOAD = config.getString(prefix, "reload", RELOAD);
            TOP = config.getString(prefix, "top", TOP);
            ITEM_NAME = config.getString(prefix, "item-name", ITEM_NAME);
            RIDE = config.getString(prefix, "ride", RIDE);
            UPPIES = config.getString(prefix, "uppies", UPPIES);
            SHAKE = config.getString(prefix, "shake", SHAKE);
            PATROL = config.getString(prefix, "patrol", PATROL);
            PATROL_STATS = config.getString(prefix, "patrol-stats", PATROL_STATS);
            SUDO = config.getString(prefix, "sudo", SUDO);
        }
    }

    public static class GENERIC {
        private static final String prefix = "generic.";

        public static String NO_PERMISSION = "<red>You don't have permission for this command</red>";
        public static String PLAYER_ONLY = "<red>This command can only be executed as a player</red>";
        public static String MUST_HOLD_ITEM = "<red>You must be holding an item to use this command.</red>";
        public static String PLAYER_NOT_ONLINE = "<red>Player [<player>] is not online</red>";

        @SuppressWarnings("unused")
        private static void load() {
            NO_PERMISSION = config.getString(prefix, "no-permission", NO_PERMISSION);
            PLAYER_ONLY = config.getString(prefix, "player-only", PLAYER_ONLY);
            MUST_HOLD_ITEM = config.getString(prefix, "must-hold-item", MUST_HOLD_ITEM);
            PLAYER_NOT_ONLINE = config.getString(prefix, "player-not-online", PLAYER_NOT_ONLINE);
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

    public static class ITEM_LORE {

        private static final String prefix = "su-command.item-lore.";

        public static String CAN_NOT_HAVE_LORE = "<red>This item cannot have lore</red>";
        public static String INVALID_LINE_NUMBER = "<red>Invalid line number</red>";
        public static String FAILED_SETTING_LORE = "<red>Failed setting lore</red>";
        public static String DONE = "<green>Set new lore to:</green>\n<lore>";

        @SuppressWarnings("unused")
        private static void load() {
            CAN_NOT_HAVE_LORE = config.getString(prefix, "can-not-have-lore", CAN_NOT_HAVE_LORE);
            INVALID_LINE_NUMBER = config.getString(prefix, "invalid-line-number", INVALID_LINE_NUMBER);
            FAILED_SETTING_LORE = config.getString(prefix, "failed-setting-lore", FAILED_SETTING_LORE);
            DONE = config.getString(prefix, "done", DONE);
        }
    }

    public static class ITEM_NAME {
        private static final String prefix = "su-command.item-name.";
        public static String DONE = "<green>Set new item name to:</green> <name>";

        @SuppressWarnings("unused")
        private static void load() {
            DONE = config.getString(prefix, "done", DONE);
        }
    }

    public static class RIDE {
        private static final String prefix = "su-command.ride.";

        public static String NO_TARGET = "<red>No target found to ride</red>";
        public static String ALREADY_HAS_PASSENGER = "<red><name> already has a passenger</red>";
        public static String UNABLE_TO_ADD_PASSENGER = "<red>Failed to add you as a passenger for <name></red>";
        public static String NOW_RIDING = "<green>Now riding <name></green>";

        @SuppressWarnings("unused")
        private static void load() {
            NO_TARGET = config.getString(prefix, "no-target", NO_TARGET);
            ALREADY_HAS_PASSENGER = config.getString(prefix, "already-has-passenger", ALREADY_HAS_PASSENGER);
            UNABLE_TO_ADD_PASSENGER = config.getString(prefix, "unable-to-add-passenger", UNABLE_TO_ADD_PASSENGER);
            NOW_RIDING = config.getString(prefix, "now-riding", NOW_RIDING);
        }
    }

    public static class UPPIES {
        private static final String prefix = "su-command.uppies.";

        public static String NO_TARGET = "<red>No target found to carry</red>";
        public static String UNABLE_TO_ADD_PASSENGER = "<red>Failed to add <name> as your passenger</red>";
        public static String ALREADY_HAVE_PASSENGER = "<red>You already have a passenger (<name>)</red>";
        public static String NOW_CARRYING = "<green>Now carrying <name></green>";
        public static String NOW_RIDING = "<green>Now riding <name></green>";

        @SuppressWarnings("unused")
        private static void load() {
            NO_TARGET = config.getString(prefix, "no-target", NO_TARGET);
            UNABLE_TO_ADD_PASSENGER = config.getString(prefix, "unable-to-add-passenger", UNABLE_TO_ADD_PASSENGER);
            ALREADY_HAVE_PASSENGER = config.getString(prefix, "already-have-passenger", ALREADY_HAVE_PASSENGER);
            NOW_CARRYING = config.getString(prefix, "now-carrying", NOW_CARRYING);
            NOW_RIDING = config.getString(prefix, "now-riding", NOW_RIDING);
        }
    }

    public static class SHAKE {
        private static final String prefix = "su-command.shake.";
        public static String NO_PASSENGERS = "<red>You have no passengers</red>";
        public static String DONE = "<green>You shook off all your passengers</green>";


        @SuppressWarnings("unused")
        private static void load() {
            NO_PASSENGERS = config.getString(prefix, "no-passengers", NO_PASSENGERS);
            DONE = config.getString(prefix, "done", DONE);
        }
    }

    public static class SUDO {
        private static final String prefix = "su-command.sudo.";
        public static String INVALID_PLAYER = "<red><player> is not a valid player</red>";
        public static String CAN_NOT_SUDO_THIS_PLAYER = "<red><player> can not be sudo'd</red>";
        public static String SUCCESS = "<green>Successfully sudo'd <player>";


        @SuppressWarnings("unused")
        private static void load() {
            INVALID_PLAYER = config.getString(prefix, "invalid-player", INVALID_PLAYER);
            CAN_NOT_SUDO_THIS_PLAYER = config.getString(prefix, "can-not-sudo-this-player", CAN_NOT_SUDO_THIS_PLAYER);
            SUCCESS = config.getString(prefix, "success", SUCCESS);
        }
    }

    public static class PATROL {
        private static final String prefix = "su-command.patrol.";

        public static String NO_PLAYERS_TO_PATROL = "<red>Found no players to patrol</red>";
        public static String PATROLLING_PLAYER = "<green>Now patrolling <player>. Last patrol: <last_patrol>.";
        public static String LIST_PLAYERS_TO_PATROL = "<green><un_patrolled_players>/<online_players> players haven't been patrolled for <minutes> minutes:\n<players>";
        public static String PLAYER_PART = "<player>";
        public static String PLAYER_PART_SEPARATOR = ", ";
        public static String CAN_NOT_PATROL_SELF = "<red>You can't patrol <player></red>";
        public static String FAILED_TO_PATROL_PLAYER = "<red>Failed to patrol <player></red>";
        public static String PATROL_REMINDER = "<rainbow>There are <amount> players that have not been patrolled for at least <time> minutes!</rainbow>";


        @SuppressWarnings("unused")
        private static void load() {
            NO_PLAYERS_TO_PATROL = config.getString(prefix, "no-players-to-patrol", NO_PLAYERS_TO_PATROL);
            PATROLLING_PLAYER = config.getString(prefix, "patrolling-player", PATROLLING_PLAYER);
            LIST_PLAYERS_TO_PATROL = config.getString(prefix, "list-players-to-patrol", LIST_PLAYERS_TO_PATROL);
            PLAYER_PART = config.getString(prefix, "player-part", PLAYER_PART);
            PLAYER_PART_SEPARATOR = config.getString(prefix, "player-part-separator", PLAYER_PART_SEPARATOR);
            CAN_NOT_PATROL_SELF = config.getString(prefix, "can-not-patrol-this-player", CAN_NOT_PATROL_SELF);
            FAILED_TO_PATROL_PLAYER = config.getString(prefix, "failed-to-patrol-player", FAILED_TO_PATROL_PLAYER);
            PATROL_REMINDER = config.getString(prefix, "patrol-reminder", PATROL_REMINDER);
        }
    }

    public static class PATROL_STATS {
        private static final String prefix = "su-command.patrol_stats.";
        public static String PART = "<green><player> has patrolled <amount> times</green>";
        public static String MESSAGE = "<green>Showing patrol stats for the last <days> days\n<players>";


        @SuppressWarnings("unused")
        private static void load() {
            PART = config.getString(prefix, "part", PART);
            MESSAGE = config.getString(prefix, "message", MESSAGE);
        }
    }
}
