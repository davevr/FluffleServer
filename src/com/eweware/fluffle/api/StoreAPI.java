package com.eweware.fluffle.api;

import com.eweware.fluffle.obj.BunnyObj;
import com.eweware.fluffle.obj.GoogleReceiptObj;
import com.eweware.fluffle.obj.PlayerObj;
import com.eweware.fluffle.rest.RestUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.model.ProductPurchase;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
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
            size1Bunnies.addAll(largeList);
            for (BunnyObj curBuns : size1Bunnies) {
                curBuns.Price = BunnyAPI.GetBuyPrice(curBuns);
            }

            return size1Bunnies;

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
        else if (storeName.compareToIgnoreCase("ad") == 0)
            didIt = ValidateAdReceipt(receipt);

        return didIt;
    }

    public static boolean ValidateAdReceipt(String receipt) {
        // todo - validate the ad receipt
        boolean didIt = false;

        didIt =  true;

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

        boolean didIt = true;
        String jsonCred = "{\n" +
                "  \"type\": \"service_account\",\n" +
                "  \"project_id\": \"api-5943446165452451243-888041\",\n" +
                "  \"private_key_id\": \"b0a50326ea70395274b7eeb1049a1dbc87ea885c\",\n" +
                "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCy2wMefte6+ZhM\\nohtHDvZ43V1zEBWZ2rcPV8yPJnxtfuokPDrsmHn7qR2UTE4Usmhea03UL8CQOb9+\\nQfDseUD3WJTMBdvS59VUgebFzDPbrdCnrKLj4/JF2Jt3XCxNk/SD+mh2GnpEuGmn\\nwDN3wuRZmMKTvHkhkDwf8kqyOqOGbcma4TzklJLQo7PlcwjzdHOtBEGR7yd2TUfg\\nnC3LAZp46mVrO0fZDGKDNKyVUWq7wvGi+4fSFie5tbzoE4RFt45cIsUWpRvD10a5\\nQ0NBkAxjleFFLGFe6zJxSW1SHklAZpNKUk66mLJDs7MJnzHpA5H8tZYCG9prBX2o\\n0GpfKbARAgMBAAECggEAKt+xSbgiQqHeTlB1tXzyvFpkMlbitrTlOPpVAOO65AyO\\nWA1QGrMaWqdZfdkkxnVV63xRddHUmT4el0d7V1RhrGlBkf69iTslulJBXZzruXdb\\nEzag/XwA8ZlQ+zXPMPGMpa57KASE1sBWI25BaC4ByzCBRCTj7JXEs+vC2CKSXIDC\\nK9p9HZ+4+bzt+zmMR/B5gDSSDFseVzuLqWH9PG1LbunHl9n84CcWB5PT3tDt5kgJ\\nee0iMOCnru65AbNWY7iliCOCD390oJvZgBJ9jd+Qcb91SL+ECbUAaX6ExrKeTou/\\nzykFtX/HPI7bl3vLZsh65Ak0r4flTqIlKaMjGvmJGQKBgQDfEiyFeHKQeemcbNNX\\n6RJDVjDA42maUVX7d+pSVKu0VzpgZyyh+oD09Ne82UlbS6jAKR390StX9AuMAnKg\\n47GiK/cv9iKgvnxM+WtOsoBxwtS7/nILSjOq7cZir6MsHsXt6t/+/hGEZV+RXr56\\nXEQ5m/viB/Wpo/bbAn05x6LRawKBgQDNQfDUnzBW2vLvOBoT5YLalPm6OMj4XLKu\\nMtP4O6njMe46HLPe90gXs40lqWBm7JmPy0y0Pa9jvZu4wsA016igqabFq7ZlP9C5\\nWfcV5KiUYbnDZyzY4U3u+yt3RbR9Wk1F7rOkRKrzp+7m/6f+pz7gwvPElh5rmF0V\\noh2p7N8XcwKBgASQfaK5hTaZmAMgYu6wkTQZhmPcA2Qp9+VuXbgTjXPiOQqR8eLa\\nmlroy6VMmOiqUqij7r4r0oQ5NSdHQYszPYZA+gzcL3c0jpyQmfaBRomNYAb8vN6o\\nRQhhVybbyy3y7z/gB3dTQY6A+ISj+KfOTYMUQwIsAYXYGgv/MArHn/hDAoGAB3ah\\nWinbqRzW/onMsPKEeow2NGZCMH22ZY0llf4dZEx5fBV1GONm9K2rsTXicnuh7c+X\\nn2oqyXaoheRW9tTspcLldISU4kOaxFKYIGyrEJIpHgjPYad3nPv3DaQ0NuakrqlK\\nUOvKR4fQsQeUxTaxm5ybHofS3Vix8cpuR7J8Pi8CgYASmjWecCpQLdF1/WFcOqEE\\n2c4kCHdQgjZGCbrpC9QFFHWh23G+zpGahwblAwuuk6+3AfiJvu2yMr4I7yx/5pK4\\nLYfGismmtXjGMHOoiY9hZRWvi5Q/Z46vlc/ATSKOXXj/SftEB0Lov885R9m7un5N\\n6Ib45mwwUyRHrf8NqFj4Rg==\\n-----END PRIVATE KEY-----\\n\",\n" +
                "  \"client_email\": \"testserviceaccount@api-5943446165452451243-888041.iam.gserviceaccount.com\",\n" +
                "  \"client_id\": \"117725182825241127491\",\n" +
                "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                "  \"token_uri\": \"https://accounts.google.com/o/oauth2/token\",\n" +
                "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/testserviceaccount%40api-5943446165452451243-888041.iam.gserviceaccount.com\"\n" +
                "}";

        try {


            //GoogleCredential credential = GoogleCredential.getApplicationDefault().createScoped(Collections.singleton("https://www.googleapis.com/auth/androidpublisher"));


            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            JsonFactory jsonFactory = new JacksonFactory();
            GoogleCredential cred2 = GoogleCredential.fromStream(new ByteArrayInputStream(jsonCred.getBytes()), httpTransport, jsonFactory);
            cred2 = cred2.createScoped(Collections.singleton("https://www.googleapis.com/auth/androidpublisher"));
            AndroidPublisher publisher = new AndroidPublisher.Builder(httpTransport, jsonFactory, cred2).setApplicationName("com.eweware.fluffle").build();
            AndroidPublisher.Purchases purchases = publisher.purchases();

            // todo - parse receipt
            GoogleReceiptObj receiptObj = RestUtils.get_gson().fromJson(receipt, GoogleReceiptObj.class);

                final AndroidPublisher.Purchases.Products.Get request = purchases.products().get(receiptObj.packageName, receiptObj.productId ,
                        receiptObj.purchaseToken);
                final ProductPurchase purchase = request.execute();
                if (purchase.getConsumptionState() > 1)
                    didIt = true;

        } catch (GoogleJsonResponseException exp) {
            log.severe(exp.getMessage());
            if ((exp.getStatusCode() == 400) && (exp.getStatusMessage() == "OK")) {
                didIt = true;
                log.severe("allowing purchase anyway");
            }
        } catch (Exception exp) {
            log.severe(exp.getMessage());
        }

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
                case "video_ad":
                    carrotsToAdd = 50;
                    break;
                case "android.test.purchased":
                case "com.eweware.fluffle.carrot01":
                case "carrot_level_01":
                    carrotsToAdd = 500;
                    break;
                case "com.eweware.fluffle.carrot02":
                case "carrot_level_02":
                    carrotsToAdd = 3000;
                    break;
                case "com.eweware.fluffle.carrot03":
                case "carrot_level_03":
                    carrotsToAdd = 7500;
                    break;
                case"com.eweware.fluffle.carrot04":
                case "carrot_level_04":
                    carrotsToAdd = 20000;
                    break;
                case "com.eweware.fluffle.carrot05":
                case "carrot_level_05":
                    carrotsToAdd = 50000;
                    break;
                default:
                    log.log(Level.SEVERE, "Invalid product specified!");
                    break;
            }

            if (carrotsToAdd > 0) {
                PlayerAPI.GiveCarrots(thePlayer, carrotsToAdd);
                newCarrots = carrotsToAdd;
            }

        }

        return newCarrots;
    }

    public static void PeriodicUpdate() {
        //todo:  update the store
    }

}
