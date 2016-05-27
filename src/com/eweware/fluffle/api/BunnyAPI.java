package com.eweware.fluffle.api;

import com.eweware.fluffle.obj.*;
import com.googlecode.objectify.Key;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.Period;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by davidvronay on 5/10/16.
 */
public class BunnyAPI {

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


    public int getCurrentValue(BunnyObj theBuns) {
        int multiplier = (int)Math.pow (2, theBuns.BunnySize - 1);
        return theBuns.Price * multiplier;
    }



    public static BunnyObj MakeRandomBunny() {
        BunnyObj newBuns = new BunnyObj ();
        double basePrice = 16;
        double totalChance = 1;
        double curChance = 1;

        newBuns.CurrentOwner = 0L;
        newBuns.OriginalOwner = 0L;
        newBuns.Female = GameAPI.Rnd().nextBoolean();

        BunnyBreedObj newBreed = GameAPI.GetRandomBreed();
        newBuns.BreedID = newBreed.id;
        newBuns.BreedName = newBreed.BreedName;
        totalChance *= curChance;

        BunnyEyeColorObj newEyeColor = BunnyBreedAPI.GetRandomEyeColor(newBreed);
        newBuns.EyeColorID = newEyeColor.id;
        newBuns.EyeColorName = newEyeColor.ColorName;
        totalChance *= curChance;

        BunnyFurColorObj newFurColor = BunnyBreedAPI.GetRandomFurColor(newBreed);
        newBuns.FurColorID = newFurColor.id;
        newBuns.FurColorName = newFurColor.ColorName;
        totalChance = GetBunnyRareness(newBuns);

        newBuns.Price = (int)(basePrice / totalChance);
        newBuns.BunnySize = 1;
        ofy().save().entity(newBuns).now();

        return newBuns;
    }

    public static double GetBunnyRareness(BunnyObj newBuns) {
        double totalChance = 1;
        double curChance;

        totalChance *= GameAPI.GetBreedChance(newBuns.BreedID);
        totalChance *= BunnyBreedAPI.GetEyeColorChance(newBuns.BreedID, newBuns.EyeColorID);
        totalChance *= BunnyBreedAPI.GetFurColorChance(newBuns.BreedID, newBuns.FurColorID);

        return totalChance;
    }

    public static BunnyObj BreedBunnies(BunnyObj momBuns, BunnyObj dadBuns) {
        BunnyObj babyBuns = null;

        if (BunniesCanBreed(momBuns,dadBuns)) {
            if (GameAPI.Rnd().nextInt(100) < GameAPI.getBreedChance()) {
                // breed them!
                babyBuns = new BunnyObj();
                babyBuns.BreedID = momBuns.BreedID;
                babyBuns.BreedName = momBuns.BreedName;

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

                // todo: make today's date
                momBuns.LastBred = new DateTime();
                dadBuns.LastBred = new DateTime();
                babyBuns.BunnySize = 1;

                // todo - save all bunnies in objectify
                Save(momBuns);
                Save(dadBuns);
                Save(babyBuns);
            }
        }

        return babyBuns;
    }

    public static Boolean CanBreed(BunnyObj theBuns) {
        if (theBuns.BunnySize > 1) {
            if (!theBuns.Female)
                return true;
            else {
                Period momBredTime = new Period(theBuns.LastBred, DateTime.now());
                if (momBredTime.getDays() > 1)
                    return true;
                else
                    return false;
            }

        } else
            return false;

    }

    public static Boolean BunniesCanBreed(BunnyObj momBuns, BunnyObj dadBuns) {

        if (CanBreed(momBuns) &&
                CanBreed(dadBuns) &&
                (momBuns.Female != dadBuns.Female) &&
                (momBuns.BunnySize == dadBuns.BunnySize) &&
                (momBuns.BreedID == dadBuns.BreedID))
            return true;
        else
            return false;
    }

    public static BunnyObj FetchById(long theId) {
        final BunnyObj foundItem = ofy().load().key(Key.create(BunnyObj.class, theId)).now();

        return foundItem;
    }


    public static void Save(BunnyObj theBuns) {
        ofy().save().entity(theBuns).now();
    }

    public static List<BunnyObj> FetchBunniesByOwner(long ownerId) {
        return ofy().load().type(BunnyObj.class).filter("CurrentOwner =", ownerId).list();
    }

    public static void RenameBunny(long theBunsId, String newName) {
        BunnyObj theBuns = FetchById(theBunsId);
        theBuns.BunnyName = newName;
        Save(theBuns);
    }

    public static void UpdateBunnyLoc(long theBunsId, int xLoc, int yLoc) {
        BunnyObj theBuns = FetchById(theBunsId);
        theBuns.HorizontalLoc = xLoc;
        theBuns.VerticalLoc = yLoc;
        Save(theBuns);
    }

}
