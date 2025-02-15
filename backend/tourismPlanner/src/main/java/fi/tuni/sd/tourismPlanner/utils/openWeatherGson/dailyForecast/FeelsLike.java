package fi.tuni.sd.tourismPlanner.utils.openWeatherGson.dailyForecast;

import com.google.gson.annotations.SerializedName;

public class FeelsLike {
    @SerializedName("day")
    private double day;

    @SerializedName("night")
    private double night;

    @SerializedName("eve")
    private double eve;
    
    @SerializedName("morn")
    private double morn;

    // Getters and setters
    public double getDay() {
        return day;
    }

    public void setDay(double day) {
        this.day = day;
    }

    public double getNight() {
        return night;
    }

    public void setNight(double night) {
        this.night = night;
    }

    public double getEve() {
        return eve;
    }

    public void setEve(double eve) {
        this.eve = eve;
    }

    public double getMorn() {
        return morn;
    }

    public void setMorn(double morn) {
        this.morn = morn;
    }
}