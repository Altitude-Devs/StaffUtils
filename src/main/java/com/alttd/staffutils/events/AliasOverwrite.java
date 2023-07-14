package com.alttd.staffutils.events;

import com.alttd.staffutils.commands.StaffUtilCommand;
import com.alttd.staffutils.commands.SubCommand;
import com.alttd.staffutils.util.Logger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.Optional;

public class AliasOverwrite implements Listener {

    private final StaffUtilCommand staffUtilCommand;
    private final Logger logger;

    public AliasOverwrite(StaffUtilCommand staffUtilCommand, Logger logger) {
        this.staffUtilCommand = staffUtilCommand;
        this.logger = logger;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event) {
        String[] args = event.getMessage().split(" ");
        if (args.length < 1) {
            logger.debug("Not enough args for alias overwrite");
            return;
        }
        if (args[0].equalsIgnoreCase("/cmi")) {
            processAsCMI(event, args);
        } else
            processAsNormal(event, args);
        //TODO this doesn't work cus cmi changes it quicker???
    }

    private void processAsCMI(PlayerCommandPreprocessEvent event, String[] args) {
        if (args.length < 2) {
            logger.debug("Not enough args for cmi alias overwrite");
            return;
        }
        Optional<SubCommand> optionalSubCommand = staffUtilCommand.getSubCommand(args[1]);
        if (optionalSubCommand.isEmpty()) {
            logger.debug("No command found to overwrite for: " + args[1]);
            return;
        }
        String newCommand = "/staffutils " + String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        event.setMessage(newCommand);
        logger.debug("Set command to: " + newCommand);
    }

    private void processAsNormal(PlayerCommandPreprocessEvent event, String[] args) {
        Optional<SubCommand> optionalSubCommand = staffUtilCommand.getSubCommand(args[0]);
        if (optionalSubCommand.isEmpty()) {
            logger.debug("No command found to overwrite for: " + args[0]);
            return;
        }
        String newCommand = "staffutils " + event.getMessage().substring(1);
        event.setMessage(newCommand);
        logger.debug("Set command to: " + newCommand);
    }

}
