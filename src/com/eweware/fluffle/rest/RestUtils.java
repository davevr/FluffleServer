package com.eweware.fluffle.rest;

import com.eweware.fluffle.api.gsonUTCDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joda.time.DateTime;

/**
 * Created by davidvronay on 5/13/16.
 */
public class RestUtils {

    private static Gson _gson = null;

    public static Gson get_gson() {
        if (_gson == null) {
            _gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new gsonUTCDateAdapter()).create();
        }

        return _gson;
    }
}
