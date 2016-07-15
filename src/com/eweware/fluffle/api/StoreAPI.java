package com.eweware.fluffle.api;

import com.eweware.fluffle.obj.BunnyObj;
import com.eweware.fluffle.obj.PlayerObj;
import com.eweware.fluffle.rest.RestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by davidvronay on 5/19/16.
 */
public class StoreAPI {
    private static final Logger log = Logger.getLogger(StoreAPI.class.getName());

    public static List<BunnyObj> FetchAvailableBunnies() {
        List<BunnyObj> size1Bunnies = ofy().load().type(BunnyObj.class).filter("CurrentOwner =",0).filter("BunnySize =", 1).list();

        if (size1Bunnies.size() > 5) {
            List<BunnyObj>  largeList = FetchAvailableLargeBunnies();

            if (largeList.size() + size1Bunnies.size() < 100) {
                size1Bunnies.addAll(largeList);
                return size1Bunnies;
            } else {
                for (int i = 0; i < 10; i++) {
                    BunnyObj curBun = size1Bunnies.remove(GameAPI.Rnd().nextInt(size1Bunnies.size()));
                    largeList.add(curBun);
                }

                return largeList;
            }
        } else {
            RepopulateStore();
            return FetchAvailableBunnies();
        }
    }

    public static List<BunnyObj> FetchAvailableLargeBunnies() {
        List<BunnyObj>  bunnyList = new ArrayList<>();

        for (int i = 2; i <= 10; i++) {
            List<BunnyObj> curSizeList = ofy().load().type(BunnyObj.class).filter("CurrentOwner =",0).filter("BunnySize =", i).limit(10).list();
            bunnyList.addAll(curSizeList);
        }

        return bunnyList;
    }

    private static void RepopulateStore() {
        for (int i = 0; i < 10; i++) {
            BunnyAPI.MakeRandomBunny();
        }
    }

    public static boolean ValidateReceipt(long userId, String storeName, String receipt) {

        boolean didIt = false;
        PlayerObj curPlayer = PlayerAPI.FetchById(userId);

        if (curPlayer.isAdmin != null && curPlayer.isAdmin)
            didIt = true;
        else if (Authenticator.IsLocalServer())
            didIt = true;
        else if (storeName.compareToIgnoreCase("apple") == 0)
            didIt = ValidateAppleReceipt(receipt);
        else if (storeName.compareToIgnoreCase("google") == 0)
            didIt = ValidateGoogleReceipt(receipt);

        return didIt;
    }

    public static boolean ValidateAppleReceipt(String receipt) {

        boolean didIt = false;
        Map<String, String> appData = new HashMap<>();
        appData.put("receipt-data", receipt);

        didIt = TryAppleAppStore(appData);
        if (!didIt)
            didIt = TryStagingAppleStore(appData);


        return didIt;
    }

    private static boolean VerifyAtURL(URL url, Map<String, String> receipt) {
        boolean didIt = false;

        try {
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            RestUtils.get_gson().toJson(receipt, writer);
            writer.close();

            int respCode = conn.getResponseCode();
            if (respCode == HttpURLConnection.HTTP_OK) {

                StringBuffer response = new StringBuffer();
                String line;

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                Map<String, Double> result = RestUtils.get_gson().fromJson(response.toString(), Map.class);
                Double theResult = result.get("status");


                if (theResult == 0)
                    didIt = true;
            }

        } catch (Exception exp) {
            log.log(Level.SEVERE, exp.getMessage());
        }

        return didIt;
    }

    private static boolean TryAppleAppStore(Map<String, String> receipt) {
        boolean didIt = false;

        try {
            URL url = new URL("https://buy.itunes.apple.com/verifyReceipt");
            didIt = VerifyAtURL(url, receipt);

        } catch (Exception exp) {
            log.log(Level.SEVERE, exp.getMessage());
        }

        return didIt;
    }

    private static boolean TryStagingAppleStore(Map<String, String> receipt) {
        boolean didIt = false;

        try {
            URL url = new URL("https://sandbox.itunes.apple.com/verifyReceipt");
            didIt = VerifyAtURL(url, receipt);

        } catch (Exception exp) {
            log.log(Level.SEVERE, exp.getMessage());
        }

        return didIt;
    }

    public static boolean ValidateGoogleReceipt(String receipt) {

        boolean didIt = false;

        return didIt;
    }


    public static int AddCredit(long userId, String productName) {
        PlayerObj thePlayer = PlayerAPI.FetchById(userId);
        int newCarrots = 0;

        if (thePlayer == null)
        {
            log.log(Level.SEVERE, "player not found for purchase");

        } else {
            int carrotsToAdd = 0;

            switch (productName) {
                case "com.eweware.fluffle.carrot01":
                    carrotsToAdd = 100;
                    break;
                case "com.eweware.fluffle.carrot02":
                    carrotsToAdd = 600;
                    break;
                case "com.eweware.fluffle.carrot03":
                    carrotsToAdd = 1500;
                    break;
                case"com.eweware.fluffle.carrot04":
                    carrotsToAdd = 4000;
                    break;
                case "com.eweware.fluffle.carrot05":
                    carrotsToAdd = 10000;
                    break;
                default:
                    log.log(Level.SEVERE, "Invalid product specified!");
                    break;
            }

            if (carrotsToAdd > 0) {
                PlayerAPI.GiveCarrots(thePlayer, carrotsToAdd);
                newCarrots = thePlayer.carrotCount;
            }

        }

        return newCarrots;
    }

    public static void PeriodicUpdate() {
        //todo:  update the store
    }

}
