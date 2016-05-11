package com.eweware.fluffle.api;

import com.eweware.fluffle.obj.BunnyBreedObj;

import java.util.ArrayList;
import java.util.Date;

import com.eweware.fluffle.obj.GameObj;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Created by davidvronay on 5/10/16.
 */
public class GameAPI {

    public static void Initialize() {} {
        LoadBunnyBreeds();
        LoadGrowthStages();
    }

    public static void UpdateGrowthStages() {
        // todo - reload growth stages
    }

    public static void UpdateBreeds() {
        // todo - reload bunny breeds
    }

    private static void LoadBunnyBreeds() {
        // todo - load the bunnies
        GameObj.BunnyBreeds = new ArrayList<>();
    }

    private static void LoadGrowthStages() {
        // todo - load the growth stages
        GameObj.GrowthStages = new ArrayList<>();

        // todo: load growth stages from DB
        GameObj.GrowthStages.add(1);
        GameObj.GrowthStages.add(10);
        GameObj.GrowthStages.add(50);
        GameObj.GrowthStages.add(100);
        GameObj.GrowthStages.add(250);
        GameObj.GrowthStages.add(500);
        GameObj.GrowthStages.add(750);
        GameObj.GrowthStages.add(1000);
        GameObj.GrowthStages.add(2500);
        GameObj.GrowthStages.add(5000);
    }

    public static DateTime getToday() {
        DateTime today = new DateTime().withTimeAtStartOfDay();

        return today;
    }

    public static BunnyBreedObj GetRandomBreed() {
        BunnyBreedObj newBreed = null;

        int totalChoices = 0;

        for (BunnyBreedObj curBreed : GameObj.BunnyBreeds) {
            totalChoices += curBreed.rarity;
        }

        int choice = GameObj.rnd.nextInt(totalChoices);
        int curLevel = 0;

        for (BunnyBreedObj curBreed : GameObj.BunnyBreeds) {
            curLevel += curBreed.rarity;
            if (choice < curLevel) {
                newBreed = curBreed;
                break;
            }
        }

        return newBreed;
    }
}
