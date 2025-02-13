package fi.tuni.sd.tourismPlanner.utils.openWeatherGson.hourlyForecast;

import com.google.gson.annotations.SerializedName;

public class Clouds {
    @SerializedName("all")
    private int all;

    // Getters and Setters
    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }
}
