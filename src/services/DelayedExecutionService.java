/*
 * Copyright (c) 2015. Dale Appleby
 */

package services;

import Util.Preconditions;
import bot.Main;
import tasks.DelayedTask;
import tasks.Task;

import java.util.*;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Unknown on 18/12/2015.
 */
public class DelayedExecutionService extends ExecutionService<DelayedTask> {
    private final DelayQueue<DelayedTask> delayedTasks = new DelayQueue<>();
    private final HashMap<DelayedTask, List<ExecutionListener>> taskListeners = new HashMap<>();

    @Override
    public void run() {
        while (executorIsRunnning) {
            try {
                DelayedTask delayedTask = delayedTasks.take();
                try {
                    if (!delayedTask.checkCondition()) {
                        notifyListeners(delayedTask, false);
                    } else {
                        delayedTask.executeTask();
                        notifyListeners(delayedTask, true);
                    }
                    taskListeners.remove(delayedTask);
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

    @Override
    public void stopServiceImmediately() {
        delayedTasks.clear();
        stopService();
    }

    @Override
    public void stopService() {
        isShuttingDown = true;
    }

    public <T extends Task<Main>> boolean executeTask(T task, long delay, TimeUnit timeUnit) {
        Preconditions.exceptionIfNull(task, timeUnit);

        if (isShuttingDown)
            return false;

        delayedTasks.offer(constructDelayedTask(task, delay, timeUnit));
        return true;
    }

    public <T extends Task<Main>> DelayedTask constructDelayedTask(T task, long delay, TimeUnit timeUnit) {
        return new DelayedTask(task.getScript(), delay, timeUnit) {
            @Override
            public boolean checkCondition() {
                return task.checkCondition();
            }

            @Override
            public void executeTask() throws InterruptedException {
                task.executeTask();
            }
        };
    }

    public <T extends Task<Main>> boolean executeTask(T task, long delay, TimeUnit timeUnit, ExecutionListener delayedExecutionListener) {
        Preconditions.exceptionIfNull(task, timeUnit, delayedExecutionListener);

        addTaskListener(task, delayedExecutionListener);
        if (!executeTask(task, delay, timeUnit)) {
            removeTaskListener(task, delayedExecutionListener);
            return false;
        }
        return true;
    }

    public <T extends Task<Main>> boolean executeTask(T task, ExecutionListener delayedExecutionListener) {
        Preconditions.exceptionIfNull(task, delayedExecutionListener);

        addTaskListener(task, delayedExecutionListener);
        if (!executeTask(task)) {
            removeTaskListener(task, delayedExecutionListener);
            return false;
        }
        return true;
    }

    @Override
    public <T extends Task<Main>> boolean executeTask(T task) {
        Objects.requireNonNull(task);

        if (isShuttingDown)
            return false;

        if (!(DelayedTask.class.isAssignableFrom(task.getClass())))
            return executeTask(task, 0, TimeUnit.MILLISECONDS);

        delayedTasks.offer((DelayedTask) task);
        return true;
    }

    public <T extends Task<Main>> void addTaskListener(T task, ExecutionListener listener) {
        Preconditions.exceptionIfNull(task, listener);

        DelayedTask delayedTask = null;

        if (DelayedTask.class.isAssignableFrom(task.getClass()))
            delayedTask = (DelayedTask) task;


        if (taskListeners.get(task) == null) {
            List<ExecutionListener> l = Collections.emptyList();
            l.add(listener);
            if (delayedTask != null) {
                taskListeners.put(delayedTask, l);
            } else {
                taskListeners.put(constructDelayedTask(task, 0, TimeUnit.MILLISECONDS), l);
            }
        } else {
            taskListeners.get(task).add(listener);
        }
    }

    public <T extends Task<Main>> void removeTaskListener(T task, ExecutionListener executionListener) {
        Preconditions.exceptionIfNull(task, executionListener);

        if (taskListeners.get(task) == null || taskListeners.get(task).size() == 0)
            return;

        for (Iterator<ExecutionListener> it = taskListeners.get(task).iterator(); it.hasNext(); ) {
            ExecutionListener cachedListener = it.next();
            if (executionListener == cachedListener) {
                it.remove();
            }
        }
    }

    private final void notifyListeners(DelayedTask task, boolean outCome) {
        Objects.requireNonNull(task);

        if (taskListeners.get(task) != null) {
            for (ExecutionListener executionListener : taskListeners.get(task))
                executionListener.onNotify(task, outCome);
        }
    }

    public DelayedTask[] getTaskArray() {
        DelayedTask[] delayedTasks1 = new DelayedTask[delayedTasks.size()];
        System.arraycopy(delayedTasks.toArray(), 0, delayedTasks1, 0, delayedTasks.size());
        return delayedTasks1;
    }
}
