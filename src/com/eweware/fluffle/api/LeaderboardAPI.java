package com.eweware.fluffle.api;

import com.eweware.fluffle.obj.BunnyObj;
import com.eweware.fluffle.obj.PlayerObj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by davidvronay on 6/2/16.
 */
public class LeaderboardAPI {

    public static List<PlayerObj> PlayersWithMostBunnies() {
        List<PlayerObj> thePlayers = ofy().load().type(PlayerObj.class).order("-totalBunnies").limit(100).list();

        return thePlayers;

    }

    public static List<PlayerObj> PlayersWithMostShares() {
        List<PlayerObj> thePlayers = ofy().load().type(PlayerObj.class).order("-totalShares").limit(100).list();

        return thePlayers;

    }

    public static List<BunnyObj> BunniesWithLargestSize() {
        List<BunnyObj> theBunnies = ofy().load().type(BunnyObj.class).order("-BunnySize").limit(100).list();

        FetchOwnerNamesAndImages(theBunnies);
        return theBunnies;

    }

    public static List<BunnyObj> BunniesWithMostShares() {
        List<BunnyObj> theBunnies = ofy().load().type(BunnyObj.class).order("-TotalShares").limit(100).list();
        FetchOwnerNamesAndImages(theBunnies);
        return theBunnies;

    }

    private static void FetchOwnerNamesAndImages(List<BunnyObj> bunList) {
        Map<Long, PlayerObj> playerList = new HashMap<>();

        for (BunnyObj curBuns : bunList) {
            if (curBuns.CurrentOwner != 0) {
                PlayerObj owner = FetchOwner(curBuns.CurrentOwner, playerList);
                curBuns.CurrentOwnerName = owner.nickname;
                curBuns.CurrentOwnerImg = owner.userimage;
            }
        }
    }

    private static PlayerObj FetchOwner(long curOwner, Map<Long, PlayerObj> playerList) {
        PlayerObj thePlayer = null;

        if (playerList.containsKey(curOwner))
            thePlayer =  playerList.get(curOwner);
        else {
            thePlayer = PlayerAPI.FetchById(curOwner);
            playerList.put(curOwner, thePlayer);
        }

        return thePlayer;
    }
}
