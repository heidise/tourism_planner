package fi.tuni.sd.tourismPlanner.utils.openWeatherGson.threeHourForecast;

import com.google.gson.annotations.SerializedName;

public class Rain {
    @SerializedName("3h")
    private double _3h;

    public double get3h() {
        return _3h;
    }

    public void set3h(double _3h) {
        this._3h = _3h;
    }
}