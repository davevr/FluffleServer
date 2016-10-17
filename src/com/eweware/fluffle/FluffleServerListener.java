package com.eweware.fluffle;

import com.eweware.fluffle.api.GameAPI;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.eweware.fluffle.obj.*;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.impl.translate.opt.joda.JodaTimeTranslators;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by davidvronay on 5/10/16.
 */
public class FluffleServerListener implements ServletContextListener {
    private static final Logger log = Logger.getLogger(FluffleServerListener.class.getName());

    public void contextInitialized(ServletContextEvent event) {
        // register our classes
        JodaTimeTranslators.add(ObjectifyService.factory());
        ObjectifyService.register(BunnyEyeColorObj.class);
        ObjectifyService.register(BunnyFurColorObj.class);
        ObjectifyService.register(BunnyBreedObj.class);
        ObjectifyService.register(BunnyObj.class);
        ObjectifyService.register(GameObj.class);
        ObjectifyService.register(PlayerObj.class);
        ObjectifyService.register(TossObj.class);
        ObjectifyService.register(ReceiptObj.class);

        // Init our Game
        try {
            ObjectifyService.run(new Work<Void>() {
                @Override
                public Void run() {
                    GameAPI.Initialize();
                    return null;
                }
            });

        }
        catch (Exception exp) {
            log.log(Level.SEVERE, exp.getMessage());
        }

    }


    public void contextDestroyed(ServletContextEvent event) {
        // App Engine does not currently invoke this method.
    }
}