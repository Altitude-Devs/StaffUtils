package com.alttd.staffutils;

import com.alttd.staffutils.commands.StaffUtilCommand;
import com.alttd.staffutils.config.Config;
import com.alttd.staffutils.config.Messages;
import com.alttd.staffutils.util.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public final class StaffUtils extends JavaPlugin {

    private Logger logger;

    @Override
    public void onEnable() {
        this.logger = new Logger(getLogger());
        registerCommands();
        registerEvents();
        reloadConfigs();
    }

    @Override
    public void onDisable() {
    }

    private void registerCommands() {
        new StaffUtilCommand(this, logger);
    }

    private void registerEvents() {
//        getServer().getPluginManager().registerEvents(, this);
    }

    public void reloadConfigs() {
        Config.reload(logger);
        Messages.reload(logger);
    }
}
