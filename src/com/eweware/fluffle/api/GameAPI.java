package com.eweware.fluffle.api;

import com.eweware.fluffle.obj.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

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
        InitEyeColors();
        InitFurColors();
        InitBunnyBreeds();
        InitGrowthStages();
        ofy().save().entity(curGame).now();
    }

    private static void InitBunnyBreeds() {
        curGame.BunnyBreeds = BunnyBreedAPI.FetchAll();
        if (curGame.BunnyBreeds.size() == 0) {
            curGame.BunnyBreeds = new ArrayList<>();
            List<BunnyEyeColorObj> eyeColors = BunnyEyeColorAPI.FetchAll();
            List<BunnyFurColorObj> furColors = BunnyFurColorAPI.FetchAll();

            BunnyBreedObj newBreed = new BunnyBreedObj();
            newBreed.BreedName = "mini lop";
            newBreed.rarity = 500;
            newBreed.possibleEyeColors = new ArrayList<>();
            BunnyBreedAPI.AddEyeColor(newBreed, eyeColors.get(0), 500);
            BunnyBreedAPI.AddEyeColor(newBreed, eyeColors.get(1), 400);
            BunnyBreedAPI.AddEyeColor(newBreed, eyeColors.get(2), 100);
            BunnyBreedAPI.AddEyeColor(newBreed, eyeColors.get(3), 10);

            newBreed.possibleFurColors = new ArrayList<>();
            BunnyBreedAPI.AddFurColor(newBreed, furColors.get(0), 500);
            BunnyBreedAPI.AddFurColor(newBreed, furColors.get(1), 500);
            BunnyBreedAPI.AddFurColor(newBreed, furColors.get(2), 100);
            BunnyBreedAPI.AddFurColor(newBreed, furColors.get(3), 100);
            ofy().save().entity(newBreed);

            newBreed = new BunnyBreedObj();
            newBreed.BreedName = "Holland Lop";
            newBreed.rarity = 400;
            newBreed.possibleEyeColors = new ArrayList<>();
            BunnyBreedAPI.AddEyeColor(newBreed, eyeColors.get(0), 500);
            BunnyBreedAPI.AddEyeColor(newBreed, eyeColors.get(1), 400);
            BunnyBreedAPI.AddEyeColor(newBreed, eyeColors.get(2), 100);
            BunnyBreedAPI.AddEyeColor(newBreed, eyeColors.get(3), 10);

            newBreed.possibleFurColors = new ArrayList<>();
            BunnyBreedAPI.AddFurColor(newBreed, furColors.get(0), 500);
            BunnyBreedAPI.AddFurColor(newBreed, furColors.get(1), 500);
            BunnyBreedAPI.AddFurColor(newBreed, furColors.get(2), 100);
            BunnyBreedAPI.AddFurColor(newBreed, furColors.get(3), 100);
            ofy().save().entity(newBreed);

            newBreed = new BunnyBreedObj();
            newBreed.BreedName = "Flemish Giant";
            newBreed.rarity = 100;
            newBreed.possibleEyeColors = new ArrayList<>();
            BunnyBreedAPI.AddEyeColor(newBreed, eyeColors.get(0), 500);
            BunnyBreedAPI.AddEyeColor(newBreed, eyeColors.get(1), 400);
            BunnyBreedAPI.AddEyeColor(newBreed, eyeColors.get(2), 100);
            BunnyBreedAPI.AddEyeColor(newBreed, eyeColors.get(3), 10);

            newBreed.possibleFurColors = new ArrayList<>();
            BunnyBreedAPI.AddFurColor(newBreed, furColors.get(0), 500);
            BunnyBreedAPI.AddFurColor(newBreed, furColors.get(1), 500);
            BunnyBreedAPI.AddFurColor(newBreed, furColors.get(2), 100);
            BunnyBreedAPI.AddFurColor(newBreed, furColors.get(3), 100);
            ofy().save().entity(newBreed);
        }


    }

    private static void InitEyeColors() {
        List<BunnyEyeColorObj> eyeColors = BunnyEyeColorAPI.FetchAll();
        if (eyeColors.size() == 0) {
            BunnyEyeColorObj newEyeColor = new BunnyEyeColorObj();
            newEyeColor.ColorName = "Brown";
            ofy().save().entity(newEyeColor).now();

            newEyeColor = new BunnyEyeColorObj();
            newEyeColor.ColorName = "Black";
            ofy().save().entity(newEyeColor).now();

            newEyeColor = new BunnyEyeColorObj();
            newEyeColor.ColorName = "Blue";
            ofy().save().entity(newEyeColor).now();

            newEyeColor = new BunnyEyeColorObj();
            newEyeColor.ColorName = "Red";
            ofy().save().entity(newEyeColor).now();
        }
    }

    private static void InitFurColors() {

        List<BunnyFurColorObj> furColors = BunnyFurColorAPI.FetchAll();
        if (furColors.size() == 0) {
            BunnyFurColorObj newFurColor = new BunnyFurColorObj();
            newFurColor.ColorName = "Brown";
            ofy().save().entity(newFurColor).now();

            newFurColor = new BunnyFurColorObj();
            newFurColor.ColorName = "Tan";
            ofy().save().entity(newFurColor).now();

            newFurColor = new BunnyFurColorObj();
            newFurColor.ColorName = "Black";
            ofy().save().entity(newFurColor).now();

            newFurColor = new BunnyFurColorObj();
            newFurColor.ColorName = "White";
            ofy().save().entity(newFurColor).now();
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
