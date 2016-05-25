package com.eweware.fluffle.api;


import com.eweware.fluffle.obj.BunnyObj;
import com.eweware.fluffle.obj.PlayerObj;
import com.eweware.fluffle.obj.TossObj;
import com.googlecode.objectify.Key;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by davidvronay on 5/10/16.
 */

public class TossAPI {

    public static TossObj FetchById(long theId) {
        final TossObj foundItem = ofy().load().key(Key.create(TossObj.class, theId)).now();

        return foundItem;
    }

    public static TossObj StartToss(long playerId, long bunnyId) {
        TossObj newObj = null;// IntStartToss(player, theNums)

        return newObj;
    }

    private static TossObj IntStartToss(PlayerObj player, BunnyObj theBuns) {
        TossObj theToss = new TossObj();


        ofy().save().entity(theToss).now();

        return theToss;
    }
}
