package ru.tulupov.weather.util;

import com.google.gson.Gson;

public class GsonUtils {
    private GsonUtils() {
        throw new UnsupportedOperationException();
    }

    public static String toJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }


}
