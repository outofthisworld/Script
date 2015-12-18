/*
 * Copyright (c) 2015. Dale Appleby
 */

package tasks;

import bot.Main;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Created by Unknown on 17/12/2015.
 * <p>
 * A class that represents a delayed Task for execution by @class DelayedExecutionService.
 * Any instances of this class should either directly supply the delayTime with the specified time unit or,
 * instantiate the class without providing a delay. If no delay is provided via the constructor
 * the task will will return a delay <= 0 and thus in most implementations execute immediately unless the getDelay()
 * and compareTo() methods are overridden to provide an accurate representation of the needed delay.
 */
public abstract class DelayedTask extends Task<Main> implements Delayed {
    private long startTime;
    private long delayTimeMS;

    public <T extends Main> DelayedTask(T script, long delayTime, TimeUnit timeUnit) {
        super(script);
        this.startTime = System.currentTimeMillis();
        this.delayTimeMS = TimeUnit.MILLISECONDS.convert(delayTime, timeUnit);
    }

    public <T extends Main> DelayedTask(T script) {
        this(script, 0, TimeUnit.MILLISECONDS);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(delayTimeMS - (System.currentTimeMillis() - startTime), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (this.getDelay(TimeUnit.SECONDS) - o.getDelay(TimeUnit.SECONDS));
    }
}
