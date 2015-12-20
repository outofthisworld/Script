/*
 * Copyright (c) 2015. Dale Appleby
 */

package services;

import Util.Preconditions;
import bot.Main;
import com.sun.istack.internal.NotNull;
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
                //Blocking call
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
    public void startService() {
        if (executorIsRunnning)
            return;

        isShuttingDown = false;
        executorIsRunnning = true;
        new Thread(this).start();
    }

    @Override
    public void stopServiceImmediately() {
        if (!executorIsRunnning || isShuttingDown)
            return;

        delayedTasks.clear();
        stopService();
    }

    @Override
    public void stopService() {
        isShuttingDown = true;
    }

    /**
     * Execute task boolean.
     *
     * @param <T>      the type parameter
     * @param task     the task
     * @param delay    the delay
     * @param timeUnit the time unit
     * @return the boolean
     */
    public <T extends Task<Main>> boolean executeTask(@NotNull T task, long delay, @NotNull TimeUnit timeUnit) {
        Preconditions.exceptionIfNull(task, timeUnit);

        if (!canAddTask())
            return false;

        delayedTasks.offer(constructDelayedTask(task, delay, timeUnit));
        return true;
    }

    /**
     * Construct delayed task delayed task.
     *
     * @param <T>      the type parameter
     * @param task     the task
     * @param delay    the delay
     * @param timeUnit the time unit
     * @return the delayed task
     */
    public <T extends Task<Main>> DelayedTask constructDelayedTask(@NotNull T task, long delay, @NotNull TimeUnit timeUnit) {
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

    /**
     * Execute task boolean.
     *
     * @param <T>                      the type parameter
     * @param task                     the task
     * @param delay                    the delay
     * @param timeUnit                 the time unit
     * @param delayedExecutionListener the delayed execution listener
     * @return the boolean
     */
    public <T extends Task<Main>> boolean executeTask(@NotNull T task, long delay, @NotNull TimeUnit timeUnit, @NotNull ExecutionListener delayedExecutionListener) {
        Preconditions.exceptionIfNull(task, timeUnit, delayedExecutionListener);

        addTaskListener(task, delayedExecutionListener);
        if (!executeTask(task, delay, timeUnit)) {
            removeTaskListener(task, delayedExecutionListener);
            return false;
        }
        return true;
    }

    /**
     * Execute task boolean.
     *
     * @param <T>                      the type parameter
     * @param task                     the task
     * @param delayedExecutionListener the delayed execution listener
     * @return the boolean
     */
    public <T extends Task<Main>> boolean executeTask(@NotNull T task, @NotNull ExecutionListener delayedExecutionListener) {
        Preconditions.exceptionIfNull(task, delayedExecutionListener);

        if (!canAddTask())
            return false;

        addTaskListener(task, delayedExecutionListener);
        if (!executeTask(task)) {
            removeTaskListener(task, delayedExecutionListener);
            return false;
        }
        return true;
    }

    private boolean canAddTask() {
        return executorIsRunnning && !isShuttingDown;
    }

    @Override
    public <T extends Task<Main>> boolean executeTask(@NotNull T task) {
        Objects.requireNonNull(task);

        if (!canAddTask())
            return false;

        if (!(DelayedTask.class.isAssignableFrom(task.getClass())))
            return executeTask(task, 0, TimeUnit.MILLISECONDS);

        delayedTasks.offer((DelayedTask) task);
        return true;
    }

    /**
     * Add task listener.
     *
     * @param <T>      the type parameter
     * @param task     the task
     * @param listener the listener
     */
    public <T extends Task<Main>> void addTaskListener(@NotNull T task, @NotNull ExecutionListener listener) {
        Preconditions.exceptionIfNull(task, listener);

        DelayedTask delayedTask = null;

        if (DelayedTask.class.isAssignableFrom(task.getClass()))
            delayedTask = (DelayedTask) task;

        if (taskListeners.get(task) == null) {
            List<ExecutionListener> l = new ArrayList<>();
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

    /**
     * Remove task listener.
     *
     * @param <T>               the type parameter
     * @param task              the task
     * @param executionListener the execution listener
     */
    public <T extends Task<Main>> void removeTaskListener(@NotNull T task, @NotNull ExecutionListener executionListener) {
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

    private final void notifyListeners(@NotNull DelayedTask task, boolean outCome) {
        Objects.requireNonNull(task);

        if (taskListeners.get(task) != null) {
            for (ExecutionListener executionListener : taskListeners.get(task))
                executionListener.onNotify(task, outCome);
        }
    }

    /**
     * Get task array delayed task [ ].
     *
     * @return the delayed task [ ]
     */
    public DelayedTask[] getTaskArray() {
        DelayedTask[] delayedTasks1 = new DelayedTask[delayedTasks.size()];
        System.arraycopy(delayedTasks.toArray(), 0, delayedTasks1, 0, delayedTasks.size());
        return delayedTasks1;
    }
}
