package com.eweware.fluffle.api;

import com.eweware.fluffle.obj.*;
import com.googlecode.objectify.Key;

import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by davidvronay on 5/10/16.
 */
public class BunnyBreedAPI {

    public static BunnyBreedObj FetchById(long theId) {
        final BunnyBreedObj foundItem = ofy().load().key(Key.create(BunnyBreedObj.class, theId)).now();

        return foundItem;
    }

    public static BunnyFurColorObj GetRandomFurColor(BunnyBreedObj theBreed) {
        BunnyFurColorObj newColor = null;

        int totalChoices = 0;

        for (FurColorRecord curColor : theBreed.possibleFurColors) {
            totalChoices += curColor.rarity;
        }

        int choice = GameAPI.Rnd().nextInt(totalChoices);
        int curLevel = 0;

        for (FurColorRecord curColor : theBreed.possibleFurColors) {
            curLevel += curColor.rarity;
            if (choice < curLevel) {
                newColor = BunnyFurColorAPI.FetchById(curColor.FurColorID);
                break;
            }
        }

        return newColor;
    }

    public static BunnyEyeColorObj GetRandomEyeColor(BunnyBreedObj theBreed) {
        BunnyEyeColorObj newColor = null;

        int totalChoices = 0;

        for (EyeColorRecord curColor : theBreed.possibleEyeColors) {
            totalChoices += curColor.rarity;
        }

        int choice = GameAPI.Rnd().nextInt(totalChoices);
        int curLevel = 0;

        for (EyeColorRecord curColor : theBreed.possibleEyeColors) {
            curLevel += curColor.rarity;
            if (choice < curLevel) {
                newColor = BunnyEyeColorAPI.FetchById(curColor.EyeColorID);
                break;
            }
        }

        return newColor;
    }

    public static double GetEyeColorChance(long breedId, long eyecolorId) {
        BunnyBreedObj newBreed = FetchById(breedId);

        int totalChoices = 0;

        for (EyeColorRecord curColor : newBreed.possibleEyeColors) {
            totalChoices += curColor.rarity;
        }

        for (EyeColorRecord curColor : newBreed.possibleEyeColors) {
            if (curColor.EyeColorID == eyecolorId) {
                return (double) curColor.rarity / (double) totalChoices;
            }
        }
        return 1L;
    }

    public static double GetFurColorChance(long breedId, long furColorId) {
        BunnyBreedObj newBreed = FetchById(breedId);

        int totalChoices = 0;

        for (FurColorRecord curColor : newBreed.possibleFurColors) {
            totalChoices += curColor.rarity;
        }

        for (FurColorRecord curColor : newBreed.possibleFurColors) {
            if (curColor.FurColorID == furColorId) {
                return (double) curColor.rarity / (double) totalChoices;
            }
        }
        return 1L;
    }

    public static void AddEyeColor (BunnyBreedObj theBreed, BunnyEyeColorObj newColor, int newChance) {
        EyeColorRecord newRec = new EyeColorRecord();
        newRec.EyeColorID = newColor.id;
        newRec.rarity = newChance;
        theBreed.possibleEyeColors.add(newRec);
    }

    public static void AddFurColor (BunnyBreedObj theBreed, BunnyFurColorObj newColor, int newChance) {
        FurColorRecord newRec = new FurColorRecord();
        newRec.FurColorID = newColor.id;
        newRec.rarity = newChance;
        theBreed.possibleFurColors.add(newRec);
    }

    public static List<BunnyBreedObj> FetchAll() {
        return ofy().load().type(BunnyBreedObj.class).list();
    }
}
