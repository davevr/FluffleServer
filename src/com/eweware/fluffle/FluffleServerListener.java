package com.eweware.fluffle;

import com.eweware.fluffle.api.GameAPI;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.eweware.fluffle.obj.*;

/**
 * Created by davidvronay on 5/10/16.
 */
public class FluffleServerListener implements ServletContextListener {


    public void contextInitialized(ServletContextEvent event) {
        // register our classes
        ObjectifyService.register(EyeColorRecord.class);
        ObjectifyService.register(FurColorRecord.class);
        ObjectifyService.register(BunnyEyeColorObj.class);
        ObjectifyService.register(BunnyFurColorObj.class);
        ObjectifyService.register(BunnyBreedObj.class);

        ObjectifyService.register(BunnyObj.class);

        //ObjectifyService.register(GameObj.class);
        ObjectifyService.register(PlayerObj.class);
        ObjectifyService.register(TossObj.class);

        // Init our Game
        GameAPI.Initialize();

    }


    public void contextDestroyed(ServletContextEvent event) {
        // App Engine does not currently invoke this method.
    }
}