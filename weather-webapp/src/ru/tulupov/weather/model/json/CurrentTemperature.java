package ru.tulupov.weather.model.json;

import com.google.gson.annotations.SerializedName;

public class CurrentTemperature {
    @SerializedName("temperature")
    private double temperature;

    @SerializedName("timestamp")
    private long timestamp;

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
