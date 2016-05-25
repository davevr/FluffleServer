package com.eweware.fluffle.api;

import com.eweware.fluffle.obj.BunnyObj;

import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by davidvronay on 5/19/16.
 */
public class StoreAPI {

    public static List<BunnyObj> FetchAvailableBunnies() {
        List<BunnyObj> availableBuns = ofy().load().type(BunnyObj.class).filter("CurrentOwner =",0).list();

        if (availableBuns.size() > 5) {
            return availableBuns;
        } else {
            RepopulateStore();
            return FetchAvailableBunnies();
        }
    }

    private static void RepopulateStore() {
        for (int i = 0; i < 10; i++) {
            BunnyAPI.MakeRandomBunny();
        }
    }

}
