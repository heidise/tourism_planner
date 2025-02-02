package fi.tuni.sd.tourismPlanner.utils;

public class WeatherData {

    public WeatherData(String WeatherDataInterval) {
        this.weatherDataInterval = WeatherDataInterval;
    }
    
    /**
     * Interval of weather data used to determine what data can be expected ('hourly', 'threeHourly','daily', 'climatic')
    */
    String weatherDataInterval;

    //*************************Data from jsons***************************************

    /**
     * Time of data calculation, unix, UTC
     * Appears in: Current weather, Hourly forecast, 3-hour forecast, Daily forecast, Climatic forecast
     */
    private long dt;

    /**
     * Time of data calculation in text format
     * Appears in: Current weather, Hourly forecast, 3-hour forecast
     */
    private String textDateTime;

    //*************************Weather attributes***************************************
    
    /**
     * Weather condition id
     * Refer to https://openweathermap.org/weather-conditions#Weather-Condition-Codes-2 for more information
     * Appears in: Current weather, Hourly forecast, 3-hour forecast, Daily forecast, Climatic forecast
     */
    private Integer weatherId;

    /**
     * Group of weather parameters (Rain, Snow, Clouds etc.)
     * Appears in: Current weather, Hourly forecast, 3-hour forecast, Daily forecast, Climatic forecast
     */
    private String weatherMain;

    /**
     * Group of weather parameters (Rain, Snow, Clouds etc.)
     * Appears in: Current weather, Hourly forecast, 3-hour forecast, Daily forecast, Climatic forecast
     */
    private String weatherDescription;

    /**
     * Weather icon id
     * Appears in: Current weather, Hourly forecast, 3-hour forecast, Daily forecast, Climatic forecast
     */
    private String weatherIcon;

    //*************************Temperature attributes***************************************

    /**
     * Temperature. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit
     * Appears in: Current weather, Hourly forecast, 3-hour forecast
     */
    private Double temp;

    /**
     * Min temperature during interval (1h, 3h, day). 
     * Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit
     * Appears in: Current weather, Hourly forecast, 3-hour forecast, Daily forecast, Climatic forecast
     */
    private Double tempMin;

    /**
     * Min temperature during interval (1h, 3h, day). 
     * Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit
     * Appears in: Current weather, Hourly forecast, 3-hour forecast, Daily forecast, Climatic forecast
     */
    private Double tempMax;

    /**
     * Morning temperature. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit
     * Appears in: Daily forecast, Climatic forecast
     */
    private Double tempMorn = null;

    /**
     * Day temperature. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit
     * Appears in: Daily forecast, Climatic forecast
     */
    private Double tempDay = null;

    /**
     * Night temperature. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit
     * Appears in: Daily forecast, Climatic forecast
     */
    private Double tempNight = null;

    /**
     * Evening temperature. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit
     * Appears in: Daily forecast, Climatic forecast
     */
    private Double tempEve = null;

    /** 
     * This temperature parameter accounts for the human perception of weather. 
     * Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit     
     * Appears in: Current weather, Hourly forecast, 3-hour forecast
     */
    private Double feelsLike = null;

    /**
     * Day temperature. Accounts for the human perception of weather. 
     * Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit
     * Appears in: Daily forecast, Climatic forecast
     */
    private Double feelsLikeDay = null;

    /**
     * Night temperature. Accounts for the human perception of weather. 
     * Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit
     * Appears in: Daily forecast, Climatic forecast
     */
    private Double feelsLikeNight = null;

    /**
     * Evening temperature. Accounts for the human perception of weather. 
     * Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit
     * Appears in: Daily forecast, Climatic forecast
     */
    private Double feelsLikeEve = null;

    /**
     * Morning temperature. Accounts for the human perception of weather. 
     * Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit
     * Appears in: Daily forecast, Climatic forecast
     */
    private Double feelsLikeMorn = null;

    //*************************Atmospheric attributes***************************************

    /**
     * Atmospheric pressure on the sea level, hPa
     * Appears in: Current weather, Hourly forecast, 3-hour forecast, Daily forecast, Climatic forecast
     */
    private int pressure;

    /**
     * Humidity, %
     * Appears in: Current weather, Hourly forecast, 3-hour forecast, Daily forecast, Climatic forecast
     */
    private int humidity;

    /**
     * Wind speed. Unit Default: meter/sec, Metric: meter/sec, Imperial: miles/hour
     * Appears in: Current weather, Hourly Forecast, 3-hour forecast, Daily forecast, Climatic forecast
     */
    private Double speed;

    /**
     * Wind gust. Unit Default: meter/sec, Metric: meter/sec, Imperial: miles/hour
     * Appears in: Current weather, Hourly Forecast, 3-hour forecast
     */
    private Double gust = null;

    /**
     * Wind direction, degrees (meteorological)
     * Appears in: Current weather, Hourly Forecast, 3-hour forecast, Daily forecast, Climatic forecast
     */
    private Integer deg;

    /**
     * Cloudiness, %
     * Appears in: Current weather, Hourly forecast, 3-hour forecast, Daily forecast, Climatic forecast
     */
    private Integer clouds;

    /**
     * Precipitation volume, mm
     * Appears in: Current weather, Hourly forecast, 3-hour forecast, Daily forecast, Climatic forecast
     */
    private Double rain;

    /**
     * Probability of precipitation
     * The values of the parameter vary between 0 and 1, where 0 is equal to 0%, 1 is equal to 100% 
     * Appears in: Current weather, Hourly forecast, 3-hour forecast
     */
    private Double pop = null;

    //*************************Getters and Setters***************************************
    
    public String getWeatherDataInterval() {
        return weatherDataInterval;
    }

    public void setWeatherDataInterval(String weatherDataInterval) {
        this.weatherDataInterval = weatherDataInterval;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public String getTextDateTime() {
        return textDateTime;
    }

    public void setTextDateTime(String textDateTime) {
        this.textDateTime = textDateTime;
    }

    public Integer getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(Integer weatherId) {
        this.weatherId = weatherId;
    }

    public String getWeatherMain() {
        return weatherMain;
    }

    public void setWeatherMain(String weatherMain) {
        this.weatherMain = weatherMain;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getTempMin() {
        return tempMin;
    }

    public void setTempMin(Double tempMin) {
        this.tempMin = tempMin;
    }

    public Double getTempMax() {
        return tempMax;
    }

    public void setTempMax(Double tempMax) {
        this.tempMax = tempMax;
    }

    public Double getTempMorn() {
        return tempMorn;
    }

    public void setTempMorn(Double tempMorn) {
        this.tempMorn = tempMorn;
    }

    public Double getTempDay() {
        return tempDay;
    }

    public void setTempDay(Double tempDay) {
        this.tempDay = tempDay;
    }

    public Double getTempNight() {
        return tempNight;
    }

    public void setTempNight(Double tempNight) {
        this.tempNight = tempNight;
    }

    public Double getTempEve() {
        return tempEve;
    }

    public void setTempEve(Double tempEve) {
        this.tempEve = tempEve;
    }

    public Double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(Double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public Double getFeelsLikeDay() {
        return feelsLikeDay;
    }

    public void setFeelsLikeDay(Double feelsLikeDay) {
        this.feelsLikeDay = feelsLikeDay;
    }

    public Double getFeelsLikeNight() {
        return feelsLikeNight;
    }

    public void setFeelsLikeNight(Double feelsLikeNight) {
        this.feelsLikeNight = feelsLikeNight;
    }

    public Double getFeelsLikeEve() {
        return feelsLikeEve;
    }

    public void setFeelsLikeEve(Double feelsLikeEve) {
        this.feelsLikeEve = feelsLikeEve;
    }

    public Double getFeelsLikeMorn() {
        return feelsLikeMorn;
    }

    public void setFeelsLikeMorn(Double feelsLikeMorn) {
        this.feelsLikeMorn = feelsLikeMorn;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getGust() {
        return gust;
    }

    public void setGust(Double gust) {
        this.gust = gust;
    }
    
    public Integer getDeg() {
        return deg;
    }

    public void setDeg(Integer deg) {
        this.deg = deg;
    }

    public Integer getClouds() {
        return clouds;
    }

    public void setClouds(Integer clouds) {
        this.clouds = clouds;
    }

    public Double getRain() {
        return rain;
    }

    public void setRain(Double rain) {
        this.rain = rain;
    }

    public Double getPop() {
        return pop;
    }

    public void setPop(Double pop) {
        this.pop = pop;
    }
    
}
