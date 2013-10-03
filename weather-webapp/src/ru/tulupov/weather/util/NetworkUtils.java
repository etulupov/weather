package ru.tulupov.weather.util;

import org.apache.commons.io.IOUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class NetworkUtils {
    private NetworkUtils() {
        throw new UnsupportedOperationException();
    }


    public static String get(String url) {

        try {
            URL page = new URL(url);
            URLConnection connection = page.openConnection();
            connection.connect();
            return IOUtils.toString(connection.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
