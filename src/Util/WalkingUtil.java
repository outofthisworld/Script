/*
 * Copyright (c) 2015. Dale Appleby
 */

package Util;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.script.Script;

import java.util.Objects;

/**
 * Created by Unknown on 14/12/2015.
 */
public final class WalkingUtil {


    private WalkingUtil() {
    }

    public static <T extends Script> boolean walkAndAwaitFinish(T script, Position position, boolean operateCamera) {
        Objects.requireNonNull(script);

        if (script.localWalker.walk(position, false)) {
            try {
                script.localWalker.waitUntilIdle();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public static <T extends Script> boolean walkAndAwaitFinish(T script, Area position, boolean operateCamera) {
        Objects.requireNonNull(script);

        if (script.localWalker.walk(position, operateCamera)) {
            try {
                script.localWalker.waitUntilIdle();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public static <T extends Script> boolean walkAndAwaitFinish(T script, NPC npcToAttack) {
        Objects.requireNonNull(script);

        if (script.localWalker.walk(npcToAttack, true)) {
            try {
                script.localWalker.waitUntilIdle();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
