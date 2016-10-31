package com.eweware.fluffle.api;

import com.eweware.fluffle.obj.*;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by davidvronay on 5/10/16.
 */
public class BunnyBreedAPI {

    public static BunnyBreedObj FetchById(long theId) {
        final BunnyBreedObj foundItem = ofy().load().key(Key.create(BunnyBreedObj.class, theId)).now();

        // load in the various subnodes
        List<BunnyFurColorObj> furList = BunnyFurColorAPI.FetchForBreed(foundItem.id);

        foundItem.possibleFurColors = furList;

        return foundItem;
    }

    public static BunnyFurColorObj GetRandomFurColor(BunnyBreedObj theBreed) {
        BunnyFurColorObj newColor = null;

        int totalChoices = 0;

        for (BunnyFurColorObj curColor : theBreed.possibleFurColors) {
            totalChoices += curColor.rarity;
        }

        int choice = GameAPI.Rnd().nextInt(totalChoices);
        int curLevel = 0;

        for (BunnyFurColorObj curColor : theBreed.possibleFurColors) {
            curLevel += curColor.rarity;
            if (choice < curLevel) {
                newColor = BunnyFurColorAPI.FetchById(curColor.id);
                break;
            }
        }

        return newColor;
    }





    public static double GetFurColorChance(long breedId, long furColorId) {
        BunnyBreedObj newBreed = FetchById(breedId);

        int totalChoices = 0;

        for (BunnyFurColorObj curColor : newBreed.possibleFurColors) {
            totalChoices += curColor.rarity;
        }

        for (BunnyFurColorObj curColor : newBreed.possibleFurColors) {
            if (curColor.id == furColorId) {
                return (double) curColor.rarity / (double) totalChoices;
            }
        }
        return 1L;
    }



    public static void AddFurColor (BunnyBreedObj theBreed, BunnyFurColorObj newColor) {
        newColor.parentBreedId = theBreed.id;
        ofy().save().entity(newColor).now();
        if (theBreed.possibleFurColors == null)
            theBreed.possibleFurColors = new ArrayList<BunnyFurColorObj>();
        theBreed.possibleFurColors.add(newColor);
    }

    public static List<BunnyBreedObj> FetchAll() {

        List<BunnyBreedObj> breedList = ofy().load().type(BunnyBreedObj.class).list();

        for (BunnyBreedObj curBreed : breedList) {
            List<BunnyFurColorObj> furList = BunnyFurColorAPI.FetchForBreed(curBreed.id);



            curBreed.possibleFurColors = furList;
        }

        return breedList;
    }
}
