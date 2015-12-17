/*
 * Copyright (c) 2015. Dale Appleby
 */

package tasks;

import bot.Main;

/**
 * Created by Unknown on 11/12/2015.
 */
public abstract class Task<T extends Main> {
    private final T script;

    public Task(T script) {
        this.script = script;
    }

    public Main getScript() {
        return script;
    }

    public abstract boolean checkCondition();

    public abstract void executeTask() throws InterruptedException;
}
