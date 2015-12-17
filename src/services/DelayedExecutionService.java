/*
 * Copyright (c) 2015. Dale Appleby
 */

package services;

import bot.Main;
import tasks.DelayedTask;
import tasks.Task;

import java.util.Objects;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Unknown on 18/12/2015.
 */
public class DelayedExecutionService extends ExecutionService<DelayedTask> {
    private final DelayQueue<DelayedTask> delayedTasks = new DelayQueue<>();

    @Override
    public void run() {
        while (executorIsRunnning) {
            try {
                DelayedTask delayedTask = delayedTasks.take();
                try {
                    if (delayedTask.checkCondition())
                        delayedTask.executeTask();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            if (isShuttingDown && delayedTasks.size() == 0)
                executorIsRunnning = false;
        }
    }

    public void stopServiceImmediately() {
        delayedTasks.clear();
        isShuttingDown = true;
    }

    @Override
    public void stopService() {
        isShuttingDown = true;
    }

    public <T extends Task<Main>> boolean executeTask(T task, long delay, TimeUnit timeUnit) {
        Objects.requireNonNull(task);

        if (isShuttingDown)
            return false;

        delayedTasks.offer(new DelayedTask(task.getScript(), delay, timeUnit) {
            @Override
            public boolean checkCondition() {
                return task.checkCondition();
            }

            @Override
            public void executeTask() throws InterruptedException {
                task.executeTask();
            }
        });

        return true;
    }

    @Override
    public <T extends Task<Main>> boolean executeTask(T task) {
        Objects.requireNonNull(task);
        
        if (!(DelayedTask.class.isAssignableFrom(task.getClass())
                || isShuttingDown))
            return false;

        delayedTasks.offer((DelayedTask) task);
        return true;
    }


    public DelayedTask[] getTaskArray() {
        DelayedTask[] delayedTasks1 = new DelayedTask[delayedTasks.size()];
        System.arraycopy(delayedTasks.toArray(), 0, delayedTasks1, 0, delayedTasks.size());
        return delayedTasks1;
    }
}
