package com.eweware.fluffle.api;

import com.eweware.fluffle.obj.BunnyFurColorObj;
import com.eweware.fluffle.obj.BunnyObj;
import com.eweware.fluffle.obj.PlayerObj;
import com.googlecode.objectify.Key;
import org.joda.time.DateTime;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by davidvronay on 5/10/16.
 */
public class PlayerAPI {



    public static PlayerObj FetchById(long theId) {
        final PlayerObj foundItem = ofy().load().key(Key.create(PlayerObj.class, theId)).now();

        return foundItem;
    }

    public static PlayerObj CreateInstance() {
        PlayerObj newPlayer = new PlayerObj();
        newPlayer.signedOn = true;
        newPlayer.lastActiveDate = new DateTime();
        BunnyObj theBuns = BunnyAPI.MakeRandomBunny();
        GiveBunny(newPlayer, theBuns);
        GiveCarrots(newPlayer, GameAPI.getStartingCarrots());

        return newPlayer;
    }

    public static void GiveBunny(PlayerObj thePlayer, BunnyObj theBuns) {
        thePlayer.totalBunnies++;
        theBuns.CurrentOwner = thePlayer.id;
        ofy().save().entity(thePlayer).now();
        ofy().save().entity(theBuns).now();
    }

    public static void GiveCarrots(PlayerObj thePlayer, int numCarrots) {
        thePlayer.carrotCount += numCarrots;
        ofy().save().entity(thePlayer).now();
    }

}
