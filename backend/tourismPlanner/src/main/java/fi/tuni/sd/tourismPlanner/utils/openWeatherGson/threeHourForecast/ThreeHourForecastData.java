package fi.tuni.sd.tourismPlanner.utils.openWeatherGson.threeHourForecast;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ThreeHourForecastData {
    @SerializedName("cod")
    private String cod;

    @SerializedName("message")
    private int message;

    @SerializedName("cnt")
    private int cnt;

    @SerializedName("list")
    private List<Forecast> list;

    @SerializedName("city")
    private City city;

    // Getters
    public String getCod() {
        return cod;
    }

    public int getMessage() {
        return message;
    }

    public int getCnt() {
        return cnt;
    }

    public List<Forecast> getList() {
        return list;
    }

    // Setters
    public void setCod(String cod) {
        this.cod = cod;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public void setList(List<Forecast> list) {
        this.list = list;
    }

    
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

}