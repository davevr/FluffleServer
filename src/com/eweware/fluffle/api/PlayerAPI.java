package com.eweware.fluffle.api;

import com.eweware.fluffle.obj.BunnyFurColorObj;
import com.eweware.fluffle.obj.PlayerObj;
import com.googlecode.objectify.Key;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by davidvronay on 5/10/16.
 */
public class PlayerAPI {

    public static PlayerObj FetchById(long theId) {
        final PlayerObj foundItem = ofy().load().key(Key.create(PlayerObj.class, theId)).now();

        return foundItem;
    }
}
