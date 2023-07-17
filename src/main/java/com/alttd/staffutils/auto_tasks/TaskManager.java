package com.alttd.staffutils.auto_tasks;

import java.util.LinkedList;

public class TaskManager implements Runnable {

    private final static TaskManager instance = new TaskManager();

    public static TaskManager getTaskManager() {
        return instance;
    }

    private final LinkedList<Task> tasks;

    private TaskManager() {
        tasks = new LinkedList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    @Override
    public void run() {
        for (Task task : tasks) {
            if (task.shouldExecute())
                task.execute();
        }
    }

    public void shutDown() {
        for (Task task : tasks) {
            task.shutDown();
        }
    }
}
