package com.alttd.staffutils.tasks;

import java.time.Duration;
import java.time.Instant;

public abstract class Task {

    abstract void execute();

    abstract Duration getFrequency();

    abstract Instant getLastExecution();

    boolean shouldExecute() {
        return Duration.between(getLastExecution(), Instant.now()).compareTo(getFrequency()) > 0;
    }

    abstract boolean shouldExecuteOnShutdown();

    void shutDown() {
        if (shouldExecuteOnShutdown()) {
            execute();
        }
    }

}
