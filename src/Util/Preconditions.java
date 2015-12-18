/*
 * Copyright (c) 2015. Dale Appleby
 */

package Util;

/**
 * Created by Unknown on 18/12/2015.
 */
public final class Preconditions {
    private Preconditions() {
    }

    public static void exceptionIfNull(Object... items) {
        for (Object item : items) {
            if (item == null)
                throw new NullPointerException();
        }
    }
}
