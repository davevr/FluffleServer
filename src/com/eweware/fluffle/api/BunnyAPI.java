package com.eweware.fluffle.api;

import com.eweware.fluffle.obj.*;
import com.googlecode.objectify.Key;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.Period;

import java.util.Calendar;
import java.util.Date;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by davidvronay on 5/10/16.
 */
public class BunnyAPI {

    public static double getProgress(BunnyObj theBuns) {
        return (double)theBuns.FeedState / (double)(CarrotsForNextSize (theBuns.BunnySize));
    }


    public static int CarrotsForNextSize(int curSize) {
        return GameObj.GrowthStages.get(curSize);
    }

    public static Boolean FeedBunny(BunnyObj theBuns) {
        Boolean leveledUp = false;
        theBuns.FeedState++;
        if (theBuns.FeedState >= CarrotsForNextSize (theBuns.BunnySize)) {
            theBuns.FeedState = 0;
            theBuns.BunnySize++;
            leveledUp = true;
        }
        theBuns.LastFeedDate = GameAPI.getToday();
        return leveledUp;
    }

    public static void StarveBunny(BunnyObj theBuns, int numDays) {
        theBuns.FeedState -= numDays;
        if (theBuns.FeedState < 0)
            theBuns.FeedState = 0;
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
        totalChance *= curChance;

        newBuns.Price = (int)(basePrice / totalChance);

        return newBuns;
    }


    public static BunnyObj BreedBunnies(BunnyObj momBuns, BunnyObj dadBuns) {
        BunnyObj babyBuns = null;

        if (BunniesCanBreed(momBuns,dadBuns)) {
            if (GameObj.rnd.nextInt(100) < GameObj.kBreedChance) {
                // breed them!
                babyBuns = new BunnyObj();
                babyBuns.BreedID = momBuns.BreedID;
                babyBuns.BreedName = momBuns.BreedName;

                if (GameObj.rnd.nextInt (10) < 5) {
                    babyBuns.FurColorID = momBuns.FurColorID;
                    babyBuns.FurColorName = momBuns.FurColorName;
                }
                else {
                    babyBuns.FurColorID = dadBuns.FurColorID;
                    babyBuns.FurColorName = dadBuns.FurColorName;
                }

                if (GameObj.rnd.nextInt (10) < 5) {
                    babyBuns.EyeColorID = momBuns.EyeColorID;
                    babyBuns.EyeColorName = momBuns.EyeColorName;
                }
                else {
                    babyBuns.EyeColorID = dadBuns.EyeColorID;
                    babyBuns.EyeColorName = dadBuns.EyeColorName;
                }

                if (GameObj.rnd.nextInt (10) < 5)
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

                // todo - save all bunnies in objectify

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

}
