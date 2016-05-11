package com.eweware.fluffle.api;

import com.eweware.fluffle.obj.*;
import com.googlecode.objectify.Key;

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

        int choice = GameObj.rnd.nextInt(totalChoices);
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

        int choice = GameObj.rnd.nextInt(totalChoices);
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
}
