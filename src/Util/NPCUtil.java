/*
 * Copyright (c) 2015. Dale Appleby
 */

package Util;

import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.script.Script;

/**
 * Created by Unknown on 15/12/2015.
 */
public final class NPCUtil {

    public static <T extends Script> boolean attackNpc(T script, NPC npcToAttack) {
        if (!npcToAttack.isOnScreen()) {
            WalkingUtil.walkAndAwaitFinish(script, npcToAttack);
        }
        return npcToAttack.interact("Attack");
    }
}
