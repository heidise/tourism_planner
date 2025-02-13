package fi.tuni.sd.tourismPlanner.utils.openWeatherGson.hourlyForecast;
import com.google.gson.annotations.SerializedName;

public class Rain {
    @SerializedName("1h")
    private double _1h;

    // Getters and Setters
    public double get_1h() {
        return _1h;
    }

    public void set_1h(double _1h) {
        this._1h = _1h;
    }
}