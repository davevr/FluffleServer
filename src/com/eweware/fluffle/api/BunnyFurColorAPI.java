package com.eweware.fluffle.api;

import com.eweware.fluffle.obj.BunnyEyeColorObj;
import com.eweware.fluffle.obj.BunnyFurColorObj;
import com.googlecode.objectify.Key;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by davidvronay on 5/10/16.
 */
public class BunnyFurColorAPI {
    private static final Logger log = Logger.getLogger(BunnyFurColorAPI.class.getName());

    public static BunnyFurColorObj FetchById(long theId) {
        final BunnyFurColorObj foundItem = ofy().load().key(Key.create(BunnyFurColorObj.class, theId)).now();

        if (foundItem != null) {
            List<BunnyEyeColorObj> eyeColors = BunnyEyeColorAPI.FetchForFurColor(foundItem.id);

            foundItem.possibleEyeColors = eyeColors;
        }

        return foundItem;
    }

    public static List<BunnyFurColorObj> FetchAll() {
        return ofy().load().type(BunnyFurColorObj.class).list();
    }

    public static BunnyEyeColorObj GetRandomEyeColor(BunnyFurColorObj theFurColor) {
        BunnyEyeColorObj newColor = null;

        int totalChoices = 0;

        for (BunnyEyeColorObj curColor : theFurColor.possibleEyeColors) {
            totalChoices += curColor.rarity;
        }

        int choice = GameAPI.Rnd().nextInt(totalChoices);
        int curLevel = 0;

        for (BunnyEyeColorObj curColor : theFurColor.possibleEyeColors) {
            curLevel += curColor.rarity;
            if (choice < curLevel) {
                newColor = BunnyEyeColorAPI.FetchById(curColor.id);
                break;
            }
        }

        return newColor;
    }

    public static void AddNewEyeColor (BunnyFurColorObj theFurColor, String eyeColor, int chance) {
        BunnyEyeColorObj    newColor = new BunnyEyeColorObj(eyeColor, chance);
        newColor.parentFurColorId = theFurColor.id;
        ofy().save().entity(newColor).now();
        if (theFurColor.possibleEyeColors == null)
            theFurColor.possibleEyeColors = new ArrayList<BunnyEyeColorObj>();
        theFurColor.possibleEyeColors.add(newColor);
    }

    public static List<BunnyFurColorObj> FetchForBreed(long breedId) {
        List<BunnyFurColorObj> furColors = ofy().load().type(BunnyFurColorObj.class).filter("parentBreedId =", breedId).list();

        if (furColors != null) {
            for (BunnyFurColorObj curColor : furColors) {
                List<BunnyEyeColorObj> eyeColors = BunnyEyeColorAPI.FetchForFurColor(curColor.id);

                curColor.possibleEyeColors = eyeColors;
            }
        }

        return furColors;
    }

    public static double GetEyeColorChance(long furColorId, long eyecolorId) {
        BunnyFurColorObj newFurColor = BunnyFurColorAPI.FetchById(furColorId);

        int totalChoices = 0;

        for (BunnyEyeColorObj curColor : newFurColor.possibleEyeColors) {
            totalChoices += curColor.rarity;
        }

        for (BunnyEyeColorObj curColor : newFurColor.possibleEyeColors) {
            if (curColor.id == eyecolorId) {
                return (double) curColor.rarity / (double) totalChoices;
            }
        }

        // if you get here, you are a mutant!
        log.log(Level.WARNING, "Rare eye color detected!");
        return .001;
    }
}
