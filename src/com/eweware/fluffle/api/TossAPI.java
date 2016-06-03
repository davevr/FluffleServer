package com.eweware.fluffle.api;


import com.eweware.fluffle.obj.BunnyObj;
import com.eweware.fluffle.obj.PlayerObj;
import com.eweware.fluffle.obj.TossObj;
import com.googlecode.objectify.Key;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Seconds;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by davidvronay on 5/10/16.
 */

public class TossAPI {
    private static final Logger log = Logger.getLogger(TossAPI.class.getName());

    public static TossObj FetchById(long theId) {
        final TossObj foundItem = ofy().load().key(Key.create(TossObj.class, theId)).now();

        return foundItem;
    }

    public static TossObj StartToss(long playerId, long bunnyId, int thePrice) {
        TossObj newObj = null;// IntStartToss(player, theNums)

        PlayerObj thePlayer = PlayerAPI.FetchById(playerId);
        BunnyObj theBuns = BunnyAPI.FetchById(bunnyId);

        if (theBuns.CurrentOwner == playerId) {
            newObj = IntStartToss(thePlayer, theBuns, thePrice);
        }
        return newObj;
    }

    private static TossObj IntStartToss(PlayerObj player, BunnyObj theBuns, int thePrice) {
        TossObj theToss = new TossObj();
        theToss.bunnyId = theBuns.id;
        theToss.tosserId = player.id;
        theToss.startTossDate = new DateTime();
        theToss.isValid = true;
        theToss.price = thePrice;

        ofy().save().entity(theToss).now();

        return theToss;
    }


    public static BunnyObj CatchToss(long catcherId, long tossId) {
        TossObj theToss = FetchById(tossId);

        if (theToss == null) {
            log.log(Level.SEVERE, "No toss for id " + tossId);
            return null;
        }

        PlayerObj theCatcher = PlayerAPI.FetchById(catcherId);

        if (theCatcher == null) {
            log.log(Level.SEVERE, "No player found for id " + catcherId);
            return null;
        }

        if (!theToss.isValid) {
            log.log(Level.WARNING, "Toss is invalid");
            return null;
        }

        if (theCatcher.carrotCount < theToss.price) {
            log.log(Level.WARNING, "catcher could not afford bunny");
            return null;
        }

        DateTime now = new DateTime();
        Seconds elapsed = Seconds.secondsBetween(now, theToss.startTossDate);

        if (elapsed.getSeconds() > 60) {
            theToss.isValid = false;
            ofy().save().entity(theToss).now();
            log.log(Level.WARNING, "Toss has expired");
            return null;
        }

        BunnyObj theBuns = BunnyAPI.FetchById(theToss.bunnyId);

        theBuns.CurrentOwner = catcherId;
        theToss.catcherId = catcherId;
        theToss.endTossDate = new DateTime();
        theToss.isValid = false;
        theBuns.TotalShares++;

        // handle exchange of money if needed
        PlayerObj tosser = PlayerAPI.FetchById(theToss.tosserId);
        tosser.totalShares++;
        if (theToss.price > 0) {
            tosser.carrotCount += theToss.price;
            theCatcher.carrotCount -= theToss.price;
        }
        ofy().save().entity(tosser).now();

        // now save!
        ofy().save().entity(theBuns).now();
        ofy().save().entity(theToss).now();

        return theBuns;

    }
}
