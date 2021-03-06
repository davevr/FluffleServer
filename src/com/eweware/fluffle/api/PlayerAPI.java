package com.eweware.fluffle.api;

import com.eweware.fluffle.obj.BunnyFurColorObj;
import com.eweware.fluffle.obj.BunnyObj;
import com.eweware.fluffle.obj.PlayerObj;
import com.googlecode.objectify.Key;
import org.joda.time.DateTime;

import java.util.List;
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

    public static int SellBunny(long userId, long bunnyId) {
        BunnyObj theBuns = BunnyAPI.FetchById(bunnyId);
        if (theBuns == null) {
            log.log(Level.SEVERE, "invalid bunny id");
            return 0;
        }

        if (theBuns.CurrentOwner != userId) {
            log.log(Level.SEVERE, "user attempt to sell a bunny she doesn't own");
            return 0;
        }

        PlayerObj   thePlayer = FetchById(userId);
        int thePrice = BunnyAPI.GetSellPrice(bunnyId);
        theBuns.CurrentOwner = 0L;
        theBuns.Price = BunnyAPI.GetBuyPrice(bunnyId);
        BunnyAPI.Save(theBuns);
        GiveCarrots(thePlayer, thePrice);
        return thePrice;
    }


    public static PlayerObj FetchByUsername(String username) {
        final List<PlayerObj> foundItems =  ofy().load().type(PlayerObj.class).filter("username =", username).list();

        if ((foundItems != null) && (foundItems.size() > 0))
            return foundItems.get(0);
        else
            return null;
    }

    public static void ChangeNickname(long userId, String newName) {

        PlayerObj thePlayer = FetchById(userId);
        thePlayer.nickname = newName;
        ofy().save().entity(thePlayer).now();
    }

    public static void ChangeUserImage(long userId, String newImageURL) {

        PlayerObj thePlayer = FetchById(userId);
        if (newImageURL.isEmpty())
            thePlayer.userimage = null;
        else
            thePlayer.userimage = newImageURL;
        ofy().save().entity(thePlayer).now();
    }

    public static PlayerObj CreateInstance() {
        PlayerObj newPlayer = new PlayerObj();
        newPlayer.signedOn = true;
        newPlayer.lastActiveDate = new DateTime();
        newPlayer.carrotCount = 0;
        newPlayer.creationDate = new DateTime();
        newPlayer.totalBunnies = 0;
        newPlayer.totalShares = 0;
        ofy().save().entity(newPlayer).now();
        BunnyObj theBuns = BunnyAPI.MakeRandomBunny();
        GiveBunny(newPlayer, theBuns);
        GiveCarrots(newPlayer, GameAPI.getStartingCarrots());

        return newPlayer;
    }

    public static void GiveBunny(long thePlayerId, BunnyObj theBuns) {
        PlayerObj thePlayer = PlayerAPI.FetchById(thePlayerId);
        if (thePlayer != null)
            GiveBunny(thePlayer, theBuns);

    }

    public static void GiveBunny(PlayerObj thePlayer, BunnyObj theBuns) {
        thePlayer.totalBunnies++;
        theBuns.CurrentOwner = thePlayer.id;
        if (theBuns.OriginalOwner == null || theBuns.OriginalOwner == 0);
            theBuns.OriginalOwner = thePlayer.id;
        ofy().save().entity(theBuns).now();
        ofy().save().entity(thePlayer).now();
    }

    public static void GiveCarrots(PlayerObj thePlayer, int numCarrots) {
        thePlayer.carrotCount += numCarrots;
        ofy().save().entity(thePlayer).now();
    }

    public static void PlayerPetBunny(long playerId, long bunnyId) {
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
        BunnyAPI.PetBunny(theBuns);
    }

    public static void PlayerFedBunny(long playerId, long bunnyId) {
        PlayerObj thePlayer = FetchById(playerId);
        if (thePlayer == null) {
            log.log(Level.SEVERE, "could not find player id " + playerId);
            return;
        }

        if (thePlayer.carrotCount == 0) {
            log.warning("Player tried to feed rabbit with no carrots");
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
        thePlayer.carrotCount--;
        ofy().save().entity(thePlayer).now();
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

        thePlayer.carrotCount -= BunnyAPI.GetBuyPrice(bunnyId);
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

        thePlayer.carrotCount += BunnyAPI.GetSellPrice(bunnyId);
        theBuns.CurrentOwner = 0L;
        theBuns.Price = BunnyAPI.GetBuyPrice(bunnyId);
        ofy().save().entity(theBuns).now();
        ofy().save().entity(thePlayer).now();
    }

    public static String GetDailyMessage(long playerId) {
        String theMsg = "";
        PlayerObj thePlayer = FetchById(playerId);

        if (thePlayer != null) {

        } else {
            log.log(Level.SEVERE, "Null player logged in");
        }
        return theMsg;
    }

}
