package com.eweware.fluffle.rest;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by davidvronay on 5/13/16.
 */
public class RestUtils {

    private static Gson _gson = null;

    public static Gson get_gson() {
        if (_gson == null) {
            _gson = Converters.registerDateTime(new GsonBuilder()).create();
        }

        return _gson;
    }
}
