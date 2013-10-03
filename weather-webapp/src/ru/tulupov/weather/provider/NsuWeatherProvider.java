package ru.tulupov.weather.provider;

import ru.tulupov.weather.model.Weather;
import ru.tulupov.weather.model.json.CurrentTemperature;
import ru.tulupov.weather.parser.NsuWeatherParser;
import ru.tulupov.weather.util.NetworkUtils;

public class NsuWeatherProvider {
    public Weather getWeather() {
        String data = NetworkUtils.get("http://weather.nsu.ru/loadata.php");
        NsuWeatherParser parser = new NsuWeatherParser(data);
        Weather weather = parser.parse();
        weather.setTimestamp(System.currentTimeMillis());
        return weather;
    }

    public CurrentTemperature getCurrentTemperature() {
        Weather weather = getWeather();

        CurrentTemperature currentTemperature = new CurrentTemperature();
        currentTemperature.setTemperature(weather.getCurrentTemperature());
        currentTemperature.setTimestamp(weather.getTimestamp());
        return currentTemperature;
    }
}
