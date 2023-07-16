package com.alttd.staffutils;

import com.alttd.staffutils.commands.StaffUtilCommand;
import com.alttd.staffutils.commands.staffutils_subcommands.Patrol;
import com.alttd.staffutils.config.Config;
import com.alttd.staffutils.config.Messages;
import com.alttd.staffutils.database.DatabaseManager;
import com.alttd.staffutils.events.AliasOverwrite;
import com.alttd.staffutils.tasks.SaveStaffPatrolActions;
import com.alttd.staffutils.tasks.TaskManager;
import com.alttd.staffutils.util.Logger;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class StaffUtils extends JavaPlugin {

    private Logger logger;
    private StaffUtilCommand staffUtilCommand;
    private ScheduledExecutorService executor;

    @Override
    public void onEnable() {
        this.logger = new Logger(getLogger());
        reloadConfigs();
        registerCommands();
        registerEvents();
        registerTasks();
    }

    @Override
    public void onDisable() {
        executor.shutdownNow();
        TaskManager.getTaskManager().shutDown();
    }

    private void registerCommands() {
        staffUtilCommand = new StaffUtilCommand(this, logger);
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new AliasOverwrite(staffUtilCommand, logger), this);
    }

    private void registerTasks() {
        DatabaseManager.initiate();
        TaskManager taskManager = TaskManager.getTaskManager();

        SaveStaffPatrolActions saveStaffPatrolActions = new SaveStaffPatrolActions(logger);
        staffUtilCommand.addSubCommand(new Patrol(saveStaffPatrolActions));
        taskManager.addTask(saveStaffPatrolActions);

        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(taskManager, Config.TASKS.CHECK_EXECUTE_FREQUENCY_MINUTES, Config.TASKS.CHECK_EXECUTE_FREQUENCY_MINUTES, TimeUnit.MINUTES);
    }

    public void reloadConfigs() {
        Config.reload(logger);
        Messages.reload(logger);
    }
}
