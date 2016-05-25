package com.eweware.fluffle.api;

import com.eweware.fluffle.obj.BunnyFurColorObj;
import com.eweware.fluffle.obj.BunnyObj;
import com.eweware.fluffle.obj.PlayerObj;
import com.googlecode.objectify.Key;
import org.joda.time.DateTime;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by davidvronay on 5/10/16.
 */
public class PlayerAPI {

    private static final Logger log = Logger.getLogger(PlayerAPI.class.getName());

    public static PlayerObj FetchById(long theId) {
        final PlayerObj foundItem = ofy().load().key(Key.create(PlayerObj.class, theId)).now();

        return foundItem;
    }

    public static PlayerObj CreateInstance() {
        PlayerObj newPlayer = new PlayerObj();
        newPlayer.signedOn = true;
        newPlayer.lastActiveDate = new DateTime();
        ofy().save().entity(newPlayer).now();
        BunnyObj theBuns = BunnyAPI.MakeRandomBunny();
        GiveBunny(newPlayer, theBuns);
        GiveCarrots(newPlayer, GameAPI.getStartingCarrots());

        return newPlayer;
    }

    public static void GiveBunny(PlayerObj thePlayer, BunnyObj theBuns) {
        thePlayer.totalBunnies++;
        theBuns.CurrentOwner = thePlayer.id;
        if (theBuns.OriginalOwner == 0);
            theBuns.OriginalOwner = thePlayer.id;
        ofy().save().entity(theBuns).now();
        ofy().save().entity(thePlayer).now();
    }

    public static void GiveCarrots(PlayerObj thePlayer, int numCarrots) {
        thePlayer.carrotCount += numCarrots;
        ofy().save().entity(thePlayer).now();
    }

    public static void PlayerFedBunny(long playerId, long bunnyId) {
        PlayerObj thePlayer = FetchById(playerId);
        if (thePlayer == null) {
            log.log(Level.SEVERE, "could not find player id " + playerId);
            return;
        }

        BunnyObj theBuns = BunnyAPI.FetchById(bunnyId);
        if (theBuns == null) {
            log.log(Level.SEVERE, "could not find bunny id " + bunnyId);
            return;
        }

        if (theBuns.CurrentOwner != playerId) {
            log.log(Level.SEVERE, "player id " + playerId + " does not own bunny id " + bunnyId);
            return;
        }

        // ok, looks good - feed it
        BunnyAPI.FeedBunny(theBuns);
    }

    public static void PlayerGotCarrots(long playerId, int numCarrots) {
        PlayerObj thePlayer = FetchById(playerId);
        if (thePlayer == null) {
            log.log(Level.SEVERE, "could not find player id " + playerId);
            return;
        }

        GiveCarrots(thePlayer, numCarrots);
    }

    public static void PlayerBoughtBunny(long playerId, long bunnyId) {
        PlayerObj thePlayer = FetchById(playerId);
        if (thePlayer == null) {
            log.log(Level.SEVERE, "could not find player id " + playerId);
            return;
        }

        BunnyObj theBuns = BunnyAPI.FetchById(bunnyId);
        if (theBuns == null) {
            log.log(Level.SEVERE, "could not find bunny id " + bunnyId);
            return;
        }

        if (theBuns.CurrentOwner != 0) {
            log.log(Level.SEVERE, "bunny id " + bunnyId + " is already owned by player id " + theBuns.CurrentOwner);
            return;
        }

        thePlayer.carrotCount -= theBuns.Price;
        PlayerAPI.GiveBunny(thePlayer, theBuns);
    }

    public static void PlayerSoldBunny(long playerId, long bunnyId) {
        PlayerObj thePlayer = FetchById(playerId);
        if (thePlayer == null) {
            log.log(Level.SEVERE, "could not find player id " + playerId);
            return;
        }

        BunnyObj theBuns = BunnyAPI.FetchById(bunnyId);
        if (theBuns == null) {
            log.log(Level.SEVERE, "could not find bunny id " + bunnyId);
            return;
        }

        if (theBuns.CurrentOwner != playerId) {
            log.log(Level.SEVERE, "player id " + playerId + " does not own bunny id " + bunnyId);
            return;
        }

        thePlayer.carrotCount += theBuns.Price;
        theBuns.CurrentOwner = 0L;
        ofy().save().entity(theBuns).now();
        ofy().save().entity(thePlayer).now();
    }

}
