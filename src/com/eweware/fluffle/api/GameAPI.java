package com.eweware.fluffle.api;

import com.eweware.fluffle.obj.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joda.time.DateTime;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by davidvronay on 5/10/16.
 */
public class GameAPI {
    private static GameObj curGame;

    public static void Initialize() {

        curGame = ofy().load().type(GameObj.class).first().now();
        if (curGame == null) {
            // no game yet - make one!
            InitFirstGame();
        } else {
            // load the breeds
            curGame.BunnyBreeds = BunnyBreedAPI.FetchAll();
        }
    }

    public static Random Rnd() {
        return curGame.rnd;
    }

    public static int getBreedChance() {
        return curGame.BreedChance;
    }

    public static List<Integer> getGrowthStages() {
        return curGame.GrowthStages;
    }

    public static int getGrowthStage(int curSize) {
        return curGame.GrowthStages.get(curSize);
    }

    public static int getStartingCarrots() {
        return curGame.StartingCarrots;
    }

    public static void UpdateGrowthStages() {
        // todo - reload growth stages
    }

    public static void UpdateBreeds() {
        // todo - reload bunny breeds
    }

    private static void InitFirstGame() {
        curGame = new GameObj();
        curGame.StartingCarrots = 250;
        InitBunnyBreeds();
        InitGrowthStages();
        ofy().save().entity(curGame).now();
    }

    private static void InitBunnyBreeds() {
        curGame.BunnyBreeds = BunnyBreedAPI.FetchAll();
        if (curGame.BunnyBreeds.size() == 0) {
            curGame.BunnyBreeds = new ArrayList<>();
            BunnyFurColorObj newFurColor = null;


            // mini lop
            BunnyBreedObj newBreed = new BunnyBreedObj("mini lop", 500);

            newBreed.possibleFurColors = new ArrayList<>();
            newFurColor = new BunnyFurColorObj("brown", 500);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor,"black", 500);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "blue", 100);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "red", 50);
            ofy().save().entity(newFurColor).now();
            BunnyBreedAPI.AddFurColor(newBreed, newFurColor);

            newFurColor = new BunnyFurColorObj("tan",500);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "black", 500);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor,"blue", 10);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "brown", 500);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor,"green", 100);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "red", 10);
            ofy().save().entity(newFurColor).now();
            BunnyBreedAPI.AddFurColor(newBreed, newFurColor);

            newFurColor = new BunnyFurColorObj("white", 100);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "black", 500);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "blue", 100);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "green", 100);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "brown", 100);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "red", 100);
            ofy().save().entity(newFurColor).now();
            BunnyBreedAPI.AddFurColor(newBreed, newFurColor);

            ofy().save().entity(newBreed);


            // holland lop
            newBreed = new BunnyBreedObj("Holland Lop", 1000);

            newFurColor = new BunnyFurColorObj("brown", 500);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "black", 500);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "blue", 100);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "red", 50);
            ofy().save().entity(newFurColor).now();
            BunnyBreedAPI.AddFurColor(newBreed, newFurColor);

            newFurColor = new BunnyFurColorObj("tan",500);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "blue", 100);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "green", 100);
            ofy().save().entity(newFurColor).now();
            BunnyBreedAPI.AddFurColor(newBreed, newFurColor);

            ofy().save().entity(newBreed);


            // holland lop
            newBreed = new BunnyBreedObj("Flemish Giant", 100);

            newFurColor = new BunnyFurColorObj("brown", 500);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "black", 500);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "blue", 100);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "red", 50);
            ofy().save().entity(newFurColor).now();
            BunnyBreedAPI.AddFurColor(newBreed, newFurColor);

            newFurColor = new BunnyFurColorObj("black",500);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "black", 500);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "blue", 100);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "brown", 500);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "green", 100);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "red", 10);
            ofy().save().entity(newFurColor).now();
            BunnyBreedAPI.AddFurColor(newBreed, newFurColor);

            newFurColor = new BunnyFurColorObj("gray", 100);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "black", 50);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "blue", 100);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "green", 100);
            BunnyFurColorAPI.AddNewEyeColor(newFurColor, "red", 100);
            ofy().save().entity(newFurColor).now();
            BunnyBreedAPI.AddFurColor(newBreed, newFurColor);
            ofy().save().entity(newBreed);
        }


    }



    private static void InitGrowthStages() {
        curGame.GrowthStages = new ArrayList<>();
        curGame.GrowthStages.add(1);
        curGame.GrowthStages.add(10);
        curGame.GrowthStages.add(50);
        curGame.GrowthStages.add(100);
        curGame.GrowthStages.add(250);
        curGame.GrowthStages.add(500);
        curGame.GrowthStages.add(750);
        curGame.GrowthStages.add(1000);
        curGame.GrowthStages.add(2500);
        curGame.GrowthStages.add(5000);

    }

    public static DateTime getToday() {
        DateTime today = new DateTime().withTimeAtStartOfDay();

        return today;
    }


    public static BunnyBreedObj GetRandomBreed() {
        BunnyBreedObj newBreed = null;

        int totalChoices = 0;

        for (BunnyBreedObj curBreed : curGame.BunnyBreeds) {
            totalChoices += curBreed.rarity;
        }

        int choice = curGame.rnd.nextInt(totalChoices);
        int curLevel = 0;

        for (BunnyBreedObj curBreed : curGame.BunnyBreeds) {
            curLevel += curBreed.rarity;
            if (choice < curLevel) {
                newBreed = curBreed;
                break;
            }
        }

        return newBreed;
    }

    public static double GetBreedChance(long breedId) {
        BunnyBreedObj newBreed = null;

        int totalChoices = 0;

        for (BunnyBreedObj curBreed : curGame.BunnyBreeds) {
            totalChoices += curBreed.rarity;
        }

        for (BunnyBreedObj curBreed : curGame.BunnyBreeds) {
            if (curBreed.id == breedId) {
                return (double) curBreed.rarity / (double) totalChoices;
            }
        }
        return 1L;
    }
}
