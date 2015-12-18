/*
 * Copyright (c) 2015. Dale Appleby
 */

package services;

import tasks.DelayedTask;

/**
 * Created by Unknown on 18/12/2015.
 */
public interface ExecutionListener {
    public void onNotify(DelayedTask task, boolean outCome);
}
