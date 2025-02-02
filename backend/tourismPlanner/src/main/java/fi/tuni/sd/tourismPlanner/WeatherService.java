package fi.tuni.sd.tourismPlanner;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import fi.tuni.sd.tourismPlanner.utils.openWeatherGson.GeocodingCity;
import fi.tuni.sd.tourismPlanner.utils.openWeatherGson.dailyForecast.DailyForecastData;
import fi.tuni.sd.tourismPlanner.utils.openWeatherGson.hourlyForecast.HourlyForecastData;
import fi.tuni.sd.tourismPlanner.utils.openWeatherGson.threeHourForecast.ThreeHourForecastData;
import fi.tuni.sd.tourismPlanner.utils.openWeatherGson.climaticForecast.ClimaticForecastData;

/**
 * Service class for fetching weather and storing data from the OpenWeatherMap API.
 */
public class WeatherService {
    private HashMap<String, HourlyForecastData> hourlyForecastDataCache = new HashMap<>();
    private HashMap<String, ThreeHourForecastData> threeHourForecastDataCache = new HashMap<>();
    private HashMap<String, DailyForecastData> dailyForecastDataCache = new HashMap<>();
    private HashMap<String, ClimaticForecastData> climaticForecastDataCache = new HashMap<>();

    private static final Gson gson = new Gson();
    private static final String BASE_URL = "https://pro.openweathermap.org/data/2.5/";

    /**
     * Fetches data from the given URL and converts it to the given data class.
     *
     * @param urlString URL to fetch data from.
     * @param dataClass Data class to convert the fetched data to.
     * @return DataClass of the given class converted from Json fetched from the given URL.
     */
    private <T> T fetchApiData(String urlString, Class<T> dataClass) {
        try {
            URL url = new URI(urlString).toURL();

            System.out.println("Fetching data from: " + url);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JsonElement jsonElement = JsonParser.parseString(response.toString());

                // Response is list when calling geocoding api
                if (jsonElement.isJsonArray()) {
                    // Nonsensical city names return empty array
                    if (jsonElement.getAsJsonArray().isEmpty()) {
                        return null;
                    } else {
                        // Currently multiple cities with same name are not supported
                        return gson.fromJson(jsonElement.getAsJsonArray().get(0), dataClass);
                    }
                } else {
                    return gson.fromJson(jsonElement, dataClass);
                }

            } else {
                System.out.println("API Call Failed. Response Code: " + responseCode);
            }
        } catch (IOException |
                 URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Fetches hourly forecast data from the OpenWeatherMap API and caches it. 
     * If the data is already fetched for given coordinates, it is returned from the cache.
     *
     * @param latitude Latitude of the location to fetch weather data for.
     * @param longitude Longitude of the location to fetch weather data for.
     * @param apiKey API key for the OpenWeatherMap API.
     * @return Hourly forecast data for the given coordinates.
     */
    private HourlyForecastData fetchHourlyForecastData(Double latitude, Double longitude, String apiKey) {
        String urlString = BASE_URL + "forecast/hourly?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiKey;
        String cacheKey = latitude.toString() + "," + longitude.toString();

        if (hourlyForecastDataCache.containsKey(cacheKey)) {
            System.out.println("Using cached hourly weather data");
            return hourlyForecastDataCache.get(cacheKey);
        }

        HourlyForecastData hourlyForecastData = fetchApiData(urlString, HourlyForecastData.class);

        hourlyForecastDataCache.put(cacheKey, hourlyForecastData);

        return hourlyForecastData;
    }

    /**
     * Fetches three hour forecast data from the OpenWeatherMap API and caches it. 
     * If the data is already fetched for given coordinates, it is returned from the cache.
     *
     * @param latitude Latitude of the location to fetch weather data for.
     * @param longitude Longitude of the location to fetch weather data for.
     * @param apiKey API key for the OpenWeatherMap API.
     * @return Three hour forecast data for the given coordinates.
     */
    private ThreeHourForecastData fetchThreeHourWeatherData(Double latitude, Double longitude, String apiKey) {
        String urlString = BASE_URL + "forecast?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiKey;

        String cacheKey = latitude.toString() + "," + longitude.toString();

        if (threeHourForecastDataCache.containsKey(cacheKey)) {
            System.out.println("Using cached three hour weather data");
            return threeHourForecastDataCache.get(cacheKey);
        }

        ThreeHourForecastData threeHourForecastData = fetchApiData(urlString, ThreeHourForecastData.class);

        threeHourForecastDataCache.put(cacheKey, threeHourForecastData);
        return threeHourForecastData;
    }

    /**
     * Fetches daily forecast data from the OpenWeatherMap API and caches it. 
     * If the data is already fetched for given coordinates, it is returned from the cache.
     *
     * @param latitude Latitude of the location to fetch weather data for.
     * @param longitude Longitude of the location to fetch weather data for.
     * @param apiKey API key for the OpenWeatherMap API.
     * @return Daily forecast data for the given coordinates.
     */
    private DailyForecastData fetchDailyWeatherData(Double latitude, Double longitude, String apiKey) {
        String urlString = BASE_URL + "forecast/daily?lat=" + latitude + "&lon=" + longitude + "&cnt=16&appid=" + apiKey;
        String cacheKey = latitude.toString() + "," + longitude.toString();

        if (dailyForecastDataCache.containsKey(cacheKey)) {
            System.out.println("Using cached daily weather data");
            return dailyForecastDataCache.get(cacheKey);
        }

        DailyForecastData dailyForecastData = fetchApiData(urlString, DailyForecastData.class);

        dailyForecastDataCache.put(cacheKey, dailyForecastData);
        return dailyForecastData;
    }

    /**
     * Fetches climatic forecast data from the OpenWeatherMap API and caches it. 
     * If the data is already fetched for given coordinates, it is returned from the cache.
     *
     * @param latitude Latitude of the location to fetch weather data for.
     * @param longitude Longitude of the location to fetch weather data for.
     * @param apiKey API key for the OpenWeatherMap API.
     * @return Climatic forecast data for the given coordinates.
     */
    private ClimaticForecastData fetchClimaticForecastData(Double latitude, Double longitude, String apiKey) {
        String urlString = BASE_URL + "forecast/climate?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiKey;
        String cacheKey = latitude.toString() + "," + longitude.toString();

        if (climaticForecastDataCache.containsKey(cacheKey)) {
            System.out.println("Using cached climatic data");
            return climaticForecastDataCache.get(cacheKey);
        }

        ClimaticForecastData climaticForecastData = fetchApiData(urlString, ClimaticForecastData.class);

        climaticForecastDataCache.put(cacheKey, climaticForecastData);

        return climaticForecastData;
    }

    /**
     * Updates weather data for the given coordinates.
     *
     * @param latitude Latitude
     * @param longitude Longitude
     * @param apiKey API key for the OpenWeatherMap API.
     * @return True if all weather data was fetched successfully, false otherwise.
     */
    public boolean updateWeatherData(Double latitude, Double longitude, String apiKey) {
        HourlyForecastData hourlyForecastData = fetchHourlyForecastData(latitude, longitude, apiKey);
        ThreeHourForecastData threeHourForecastData = fetchThreeHourWeatherData(latitude, longitude, apiKey);
        DailyForecastData dailyForecastData = fetchDailyWeatherData(latitude, longitude, apiKey);
        ClimaticForecastData climaticForecastData = fetchClimaticForecastData(latitude, longitude, apiKey);

        return  hourlyForecastData != null
                && threeHourForecastData != null
                && dailyForecastData != null
                && climaticForecastData != null;
    }

    /**
     * Getter for hourly forecast data for the given coordinates.
     *
     * @param latitude Latitude
     * @param longitude Longitude
     * @return Hourly forecast data for the given coordinates.
     */
    public HourlyForecastData getHourlyForecastData(Double latitude, Double longitude) {
        String cacheKey = latitude.toString() + "," + longitude.toString();

        if (!hourlyForecastDataCache.containsKey(cacheKey)) {
            throw new IllegalStateException("Hourly weather data not fetched yet for the given coordinates.");
        }
        return hourlyForecastDataCache.get(cacheKey);
    }

    /**
     * Getter for three hour forecast data for the given coordinates.
     *
     * @param latitude Latitude
     * @param longitude Longitude
     * @return Three hour forecast data for the given coordinates.
     */
    public ThreeHourForecastData getThreeHourForecastData(Double latitude, Double longitude) {
        String cacheKey = latitude.toString() + "," + longitude.toString();

        if (!threeHourForecastDataCache.containsKey(cacheKey)) {
            throw new IllegalStateException("Three hour weather data not fetched yet for the given coordinates.");
        }
        return threeHourForecastDataCache.get(cacheKey);
    }

    /**
     * Getter for daily forecast data for the given coordinates.
     *
     * @param latitude Latitude
     * @param longitude Longitude
     * @return Daily forecast data for the given coordinates.
     */
    public DailyForecastData getDailyForecastData(Double latitude, Double longitude) {
        String cacheKey = latitude.toString() + "," + longitude.toString();

        if (!dailyForecastDataCache.containsKey(cacheKey)) {
            throw new IllegalStateException("Daily weather data not fetched yet for the given coordinates.");
        }
        return dailyForecastDataCache.get(cacheKey);
    }

    /**
     * Getter for climatic forecast data for the given coordinates.
     *
     * @param latitude Latitude
     * @param longitude Longitude
     * @return Climatic forecast data for the given coordinates.
     */
    public ClimaticForecastData getClimaticForecastData(Double latitude, Double longitude) {

        String cacheKey = latitude.toString() + "," + longitude.toString();

        if (!climaticForecastDataCache.containsKey(cacheKey)) {
            throw new IllegalStateException("Climatic weather data not fetched yet for the given coordinates.");
        }
        return climaticForecastDataCache.get(cacheKey);
    }

    /**
     * Fetches geocoding data for the given city name.
     *
     * @param cityName City name to fetch geocoding data for.
     * @param apiKey API key for the OpenWeatherMap API.
     * @return Geocoding data for the given city name.
     */
    public GeocodingCity getGeocodingCityData(String cityName, String apiKey) {
        String urlString = String.format("http://pro.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s", 
                                        cityName.replace(' ', '+'), apiKey);
        return fetchApiData(urlString, GeocodingCity.class);
    }
}