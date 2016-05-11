package com.eweware.fluffle.api;

import com.eweware.fluffle.obj.BunnyFurColorObj;
import com.googlecode.objectify.Key;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by davidvronay on 5/10/16.
 */
public class BunnyFurColorAPI {

    public static BunnyFurColorObj FetchById(long theId) {
        final BunnyFurColorObj foundItem = ofy().load().key(Key.create(BunnyFurColorObj.class, theId)).now();

        return foundItem;
    }
}
