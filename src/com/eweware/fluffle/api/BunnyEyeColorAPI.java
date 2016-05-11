package com.eweware.fluffle.api;

import com.eweware.fluffle.obj.BunnyEyeColorObj;
import com.googlecode.objectify.Key;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by davidvronay on 5/10/16.
 */
public class BunnyEyeColorAPI {

    public static BunnyEyeColorObj FetchById(long theId) {
        final BunnyEyeColorObj foundItem = ofy().load().key(Key.create(BunnyEyeColorObj.class, theId)).now();

        return foundItem;
    }
}
