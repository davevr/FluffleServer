package com.eweware.fluffle.api;

import com.eweware.fluffle.obj.*;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.repackaged.com.google.protobuf.Duration;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import org.joda.time.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by davidvronay on 5/10/16.
 */
public class BunnyAPI {
    private static final Logger log = Logger.getLogger(BunnyAPI.class.getName());
    public static double kBasePrice = 64;

    public static double getProgress(BunnyObj theBuns) {
        return (double)theBuns.FeedState / (double)(CarrotsForNextSize (theBuns.BunnySize));
    }


    public static int CarrotsForNextSize(int curSize) {
        return GameAPI.getGrowthStage(curSize);
    }

    public static Boolean FeedBunny(BunnyObj theBuns) {
        Boolean leveledUp = false;
        theBuns.FeedState++;
        if (theBuns.FeedState >= CarrotsForNextSize (theBuns.BunnySize)) {
            theBuns.FeedState = 0;
            theBuns.BunnySize++;
            leveledUp = true;
        }
        theBuns.LastFeedDate = new DateTime();
        ofy().save().entity(theBuns).now();
        return leveledUp;
    }

    public static Boolean PetBunny(BunnyObj theBuns) {
        Boolean leveledUp = false;

        if (theBuns.Happiness != 100) {
            theBuns.Happiness++;
            if (theBuns.Happiness >= 100) {
                theBuns.Happiness = 100;
                leveledUp = true;
            }
            ofy().save().entity(theBuns).now();
        }
        return leveledUp;
    }

    public static void StarveBunny(BunnyObj theBuns, int numDays) {
        theBuns.FeedState -= numDays;
        if (theBuns.FeedState < 0)
            theBuns.FeedState = 0;
        ofy().save().entity(theBuns).now();
    }

    public static int getTotalCarrots(BunnyObj theBuns) {
        int count = 0;
        int startLevel = theBuns.BunnySize - 1;

        while (startLevel-- > 0) {
            count += CarrotsForNextSize (startLevel);
        }
        count += theBuns.FeedState;

        return count;
    }




    public static BunnyObj MakeRandomBunny() {
        BunnyObj newBuns = new BunnyObj ();
        double totalChance = 1;

        newBuns.CurrentOwner = 0L;
        newBuns.OriginalOwner = 0L;
        newBuns.TotalShares = 0;
        newBuns.Female = GameAPI.Rnd().nextBoolean();

        BunnyBreedObj newBreed = GameAPI.GetRandomBreed();
        newBuns.BreedID = newBreed.id;
        newBuns.BreedName = newBreed.BreedName;

        BunnyFurColorObj newFurColor = BunnyBreedAPI.GetRandomFurColor(newBreed);
        newBuns.FurColorID = newFurColor.id;
        newBuns.FurColorName = newFurColor.ColorName;

        BunnyEyeColorObj newEyeColor = BunnyFurColorAPI.GetRandomEyeColor(newFurColor);
        newBuns.EyeColorID = newEyeColor.id;
        newBuns.EyeColorName = newEyeColor.ColorName;

        newBuns.BunnySize = 1;
        newBuns.Price = GetSellPrice(newBuns);
        ofy().save().entity(newBuns).now();

        return newBuns;
    }

    public static double GetBunnyRareness(BunnyObj newBuns) {
        double totalChance = 1;
        double curChance;

        totalChance *= GameAPI.GetBreedChance(newBuns.BreedID);
        totalChance *= BunnyBreedAPI.GetFurColorChance(newBuns.BreedID, newBuns.FurColorID);
        totalChance *= BunnyFurColorAPI.GetEyeColorChance(newBuns.FurColorID, newBuns.EyeColorID);
        return totalChance;
    }

    public static BunnyObj BreedBunnies(long momBunsId, long dadBunsId) {
        BunnyObj momBuns = FetchById(momBunsId);
        BunnyObj dadBuns = FetchById(dadBunsId);

        if (momBuns != null && dadBuns != null) {
            return BreedBunnies(momBuns, dadBuns);
        } else {
            log.severe("Attempt to breed bunnies with invalid IDs");
            return null;
        }
    }

    public static BunnyObj BreedBunnies(BunnyObj momBuns, BunnyObj dadBuns) {
        BunnyObj babyBuns = null;

        if (BunniesCanBreed(momBuns,dadBuns)) {
            if (GameAPI.Rnd().nextInt(100) < GameAPI.getBreedChance()) {
                // breed them!
                babyBuns = new BunnyObj();
                babyBuns.BreedID = momBuns.BreedID;
                babyBuns.BreedName = momBuns.BreedName;

                // to do:  check for mutations
                if (GameAPI.Rnd().nextInt (10) < 5) {
                    babyBuns.FurColorID = momBuns.FurColorID;
                    babyBuns.FurColorName = momBuns.FurColorName;
                }
                else {
                    babyBuns.FurColorID = dadBuns.FurColorID;
                    babyBuns.FurColorName = dadBuns.FurColorName;
                }

                if (GameAPI.Rnd().nextInt (10) < 5) {
                    babyBuns.EyeColorID = momBuns.EyeColorID;
                    babyBuns.EyeColorName = momBuns.EyeColorName;
                }
                else {
                    babyBuns.EyeColorID = dadBuns.EyeColorID;
                    babyBuns.EyeColorName = dadBuns.EyeColorName;
                }

                if (GameAPI.Rnd().nextInt (10) < 5)
                    babyBuns.Female = true;
                else
                    babyBuns.Female = false;

                if (momBuns.Female) {
                    babyBuns.MotherID = momBuns.id;
                    babyBuns.FatherID = dadBuns.id;
                } else {
                    babyBuns.MotherID = dadBuns.id;
                    babyBuns.FatherID = momBuns.id;
                }

                momBuns.LastBred = new DateTime();
                dadBuns.LastBred = new DateTime();
                babyBuns.BunnySize = 1;
                Save(momBuns);
                Save(dadBuns);
                PlayerAPI.GiveBunny(momBuns.CurrentOwner, babyBuns);
            }
        }

        return babyBuns;
    }


    public static Boolean CanBreed(BunnyObj theBuns) {
        if (theBuns.BunnySize > 1) {
            if (!theBuns.Female)
                return true;
            else if (theBuns.LastBred == null)
                return true;    // buns has never bred...
            else {
                int totalSeconds = Seconds.secondsBetween(DateTime.now(), theBuns.LastBred).getSeconds();
                if (totalSeconds > 86400)   // wait about a day
                    return true;
                else {
                    return false;
                }
            }

        } else {
            return false;
        }
    }

    public static Boolean BunniesCanBreed(BunnyObj momBuns, BunnyObj dadBuns) {

        if (CanBreed(momBuns) &&
                CanBreed(dadBuns) &&
                (momBuns.Female != dadBuns.Female) &&
                (momBuns.BunnySize == dadBuns.BunnySize) &&
                ((long)momBuns.BreedID == (long)dadBuns.BreedID))
            return true;
        else
            return false;
    }

    public static BunnyObj FetchById(long theId) {
        final BunnyObj foundItem = ofy().load().key(Key.create(BunnyObj.class, theId)).now();

        return foundItem;
    }

    public static int GetSellPrice(long bunnyId) {
        BunnyObj theBuns = FetchById(bunnyId);

        return GetSellPrice(theBuns);
    }

    public static int GetSellPrice(BunnyObj theBuns) {
        double totalChance = GetBunnyRareness(theBuns);
        int multiplier = 2;
        int curPrice = (int)kBasePrice;

        for (int i = 1; i < theBuns.BunnySize; i++) {
            curPrice += CarrotsForNextSize(i) * multiplier;
            multiplier++;
        }

        if (totalChance < .02) {
            curPrice *= 2;
            log.info("Rare bunny id-" + theBuns.id.toString());
        }
        else if (totalChance < 0.001) {
            curPrice *= 10;
            log.info("Very rare bunny id-" + theBuns.id.toString());
        }

        return curPrice;
    }

    private static int GetTotalCarrots(BunnyObj buns) {
        int carrotCount = 0;
        for (int i = 0; i < buns.BunnySize; i++) {
            carrotCount += CarrotsForNextSize(i);
        }
        carrotCount += buns.FeedState;

        return carrotCount;
    }
    public static int GetBuyPrice(long bunnyId) {
       return GetSellPrice(bunnyId) * 2;
    }

    public static int GetBuyPrice(BunnyObj theBuns) {
        return GetSellPrice(theBuns) * 2;
    }


    public static void Save(BunnyObj theBuns) {
        ofy().save().entity(theBuns).now();
    }

    public static List<BunnyObj> FetchBunniesByOwner(long ownerId) {
        return ofy().load().type(BunnyObj.class).filter("CurrentOwner =", ownerId).list();
    }

    public static void RenameBunny(long playerId, long theBunsId, String newName) {
        BunnyObj theBuns = FetchById(theBunsId);
        if ((theBuns.BunnyName == null) || (theBuns.OriginalOwner == 0L) || (theBuns.OriginalOwner == playerId)) {
            theBuns.BunnyName = newName;
            Save(theBuns);
        }
    }

    public static void UpdateBunnyLoc(long theBunsId, int xLoc, int yLoc) {
        BunnyObj theBuns = FetchById(theBunsId);
        if (xLoc < -100)
            xLoc = -100;
        else if (xLoc > 100)
            xLoc = 100;

        if (yLoc < -100)
            yLoc = -100;
        else if (yLoc > 100)
            yLoc = 100;

        theBuns.HorizontalLoc = xLoc;
        theBuns.VerticalLoc = yLoc;
        Save(theBuns);
    }

    public static List<BunnyObj> FetchAllBunnies(String cursorString, int maxCount) {
        Query<BunnyObj> query = ofy().load().type(BunnyObj.class).limit(maxCount);

        if (cursorString != null)
            query = query.startAt(Cursor.fromWebSafeString(cursorString));

        return query.list();
    }

    public static void CheckAllBunniesForBreeding() {
        List<PlayerObj> playerList = ofy().load().type(PlayerObj.class).list();
        int numNewBuns = 0;

        for (PlayerObj curPlayer : playerList) {
            numNewBuns += CheckPlayerBunniesForBreeding(curPlayer);
        }

        log.info(String.format("created %d new bunnies", numNewBuns));
    }

    private static int CheckPlayerBunniesForBreeding(PlayerObj thePlayer) {
        List<BunnyObj> playerBuns = FetchBunniesByOwner(thePlayer.id);
        int numNewBuns = 0;

        if ((playerBuns.size() > 1) && (playerBuns.size() < 50)) {
            for (int i = 0; i < playerBuns.size() - 1; i++) {
                BunnyObj curBuns = playerBuns.get(i);
                for (int j = i + 1; j < playerBuns.size(); j++) {
                    BunnyObj secondBuns = playerBuns.get(j);
                    BunnyObj babyBuns = BreedBunnies(curBuns, secondBuns);
                    if (babyBuns != null)
                        numNewBuns++;
                }
            }
        }

        return numNewBuns;
    }

    public static void StarveAllBunnies() {
        List<PlayerObj> playerList = ofy().load().type(PlayerObj.class).list();

        for (PlayerObj curPlayer : playerList) {
            List<BunnyObj> playerBuns = FetchBunniesByOwner(curPlayer.id);
            for (BunnyObj curBuns : playerBuns) {
                StarveBunny(curBuns, 1);
            }
        }
    }

}
