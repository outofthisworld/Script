/*
 * Copyright (c) 2015. Dale Appleby
 */

package services;

import bot.Main;
import tasks.Task;

/**
 * Created by Unknown on 17/12/2015.
 */
public interface Service {
    public void stopServiceImmediately();

    public void stopService();

    public void startService();

    public <T extends Task<Main>> boolean executeTask(T task);
}
