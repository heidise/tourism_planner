package fi.tuni.sd.tourismPlanner.utils.openWeatherGson.hourlyForecast;

import com.google.gson.annotations.SerializedName;

public class Coord {
    @SerializedName("lat")
    private double lat;

    @SerializedName("lon")
    private double lon;

    // Getters and Setters
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}