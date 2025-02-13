package fi.tuni.sd.tourismPlanner.utils.openWeatherGson.hourlyForecast;

import com.google.gson.annotations.SerializedName;

public class Sys {
    @SerializedName("pod")
    private String pod;

    // Getters and Setters
    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }
}
