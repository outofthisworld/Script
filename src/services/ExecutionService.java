/*
 * Copyright (c) 2015. Dale Appleby
 */

package services;

import tasks.Task;

/**
 * Created by Unknown on 17/12/2015.
 */
public abstract class ExecutionService<T extends Task> implements Runnable, Service {
    protected volatile boolean executorIsRunnning = false;
    protected volatile boolean isShuttingDown = false;
}
