/*
 * Copyright (c) 2015. Dale Appleby
 */

package tasks.misc;

import bot.Main;
import tasks.DelayedTask;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Created by Unknown on 18/12/2015.
 */
public class WorldHopper extends DelayedTask {

    public <T extends Main> WorldHopper(T script) {
        super(script);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(12, TimeUnit.SECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return super.compareTo(o);
    }

    @Override
    public boolean checkCondition() {
        return false;
    }

    @Override
    public void executeTask() throws InterruptedException {

    }
}
