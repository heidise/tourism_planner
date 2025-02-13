package fi.tuni.sd.tourismPlanner.utils.openWeatherGson.climaticForecast;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ClimaticForecastData {
    @SerializedName("city")
    private City city;

    @SerializedName("cod")
    private String code;

    @SerializedName("message")
    private double message;

    @SerializedName("cnt")
    private int cnt;

    @SerializedName("list")
    private List<Forecast> list;

    // Getters and Setters
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getMessage() {
        return message;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public List<Forecast> getList() {
        return list;
    }

    public void setList(List<Forecast> list) {
        this.list = list;
    }
}
