package com.eweware.fluffle.api;

import com.eweware.fluffle.obj.BunnyObj;
import com.eweware.fluffle.obj.PlayerObj;

import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by davidvronay on 6/2/16.
 */
public class LeaderboardAPI {

    public static List<PlayerObj> PlayersWithMostBunnies() {
        List<PlayerObj> thePlayers = ofy().load().type(PlayerObj.class).order("totalBunnies").limit(100).list();

        return thePlayers;

    }

    public static List<PlayerObj> PlayersWithMostShares() {
        List<PlayerObj> thePlayers = ofy().load().type(PlayerObj.class).order("totalShares").limit(100).list();

        return thePlayers;

    }

    public static List<BunnyObj> BunniesWithLargestSize() {
        List<BunnyObj> theBunnies = ofy().load().type(BunnyObj.class).order("BunnySize").limit(100).list();

        return theBunnies;

    }

    public static List<BunnyObj> BunniesWithMostShares() {
        List<BunnyObj> theBunnies = ofy().load().type(BunnyObj.class).order("TotalShares").limit(100).list();

        return theBunnies;

    }
}
