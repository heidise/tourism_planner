package fi.tuni.sd.tourismPlanner;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.function.Function;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import fi.tuni.sd.tourismPlanner.utils.WeatherData;
import fi.tuni.sd.tourismPlanner.utils.openWeatherGson.climaticForecast.ClimaticForecastData;
import fi.tuni.sd.tourismPlanner.utils.openWeatherGson.dailyForecast.DailyForecastData;
import fi.tuni.sd.tourismPlanner.utils.openWeatherGson.threeHourForecast.ThreeHourForecastData;
import fi.tuni.sd.tourismPlanner.utils.openWeatherGson.hourlyForecast.HourlyForecastData;

/**
 * WeatherController class is responsible for handling weather gotten from WeatherService, aggregation of weather data and converting it to JSON string.
 */
public class WeatherController {

    private WeatherService weatherService;
    private final String WEATHER_CATEGORY_ALL = "All";

    /**
     * Constructor for WeatherController
     *
     * @param weatherService
     */
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Checks if the date, location and weather criteria match.
     *
     * @param weatherCriteria The weather criteria to be checked
     * @param date            The date to be checked
     * @param lat             latidtude for the location
     * @param lon             longitude for the location
     * @return boolean true if the date, location and weather criteria match, false otherwise
     */
    public boolean dateLocationMatchWeatherCriteria(String weatherCriteria, Long date, Double lat, Double lon) {
        System.out.println("dateLocationMatchWeatherCriteria: coordinates: " + lat + ", " + lon + "date: " + date + " and weatherCriteria: " + weatherCriteria);

        if (weatherCriteria.equals(WEATHER_CATEGORY_ALL)) {
            return true;
        }

        DailyForecastData dailyForecastData = weatherService.getDailyForecastData(lat, lon);
        ClimaticForecastData climaticForecastData = weatherService.getClimaticForecastData(lat, lon);

        LocalDate localDate = Instant.ofEpochSecond(date).atZone(ZoneId.systemDefault()).toLocalDate();

        Long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), localDate);

        try {
            int weatherCode = Integer.parseInt(weatherCriteria);

            if (daysBetween <= 15) {
                List<fi.tuni.sd.tourismPlanner.utils.openWeatherGson.dailyForecast.Forecast> forecasts = dailyForecastData.getList();
                System.out.println("Forecasts size: " + forecasts.size());
                System.out.println("Days between: " + daysBetween);
                fi.tuni.sd.tourismPlanner.utils.openWeatherGson.dailyForecast.Forecast forecast = forecasts.get(daysBetween.intValue());
                System.out.println("Weather criteria: " + weatherCriteria + " and forecast weather: " + forecast.getWeather().get(0).getId());
                return Math.floor(forecast.getWeather().get(0).getId()) == Math.floor(weatherCode);

            } else {
                List<fi.tuni.sd.tourismPlanner.utils.openWeatherGson.climaticForecast.Forecast> forecasts = climaticForecastData.getList();
                fi.tuni.sd.tourismPlanner.utils.openWeatherGson.climaticForecast.Forecast forecast = forecasts.get(daysBetween.intValue());
                System.out.println("Weather criteria: " + weatherCriteria + " and forecast weather: " + forecast.getWeather().get(0).getId());
                return Math.floor(forecast.getWeather().get(0).getId()) == Math.floor(weatherCode);
            }

        } catch (NumberFormatException e) {
            if (daysBetween <= 15) {
                List<fi.tuni.sd.tourismPlanner.utils.openWeatherGson.dailyForecast.Forecast> forecasts = dailyForecastData.getList();
                System.out.println("Forecasts size: " + forecasts.size());
                System.out.println("Days between: " + daysBetween);
                fi.tuni.sd.tourismPlanner.utils.openWeatherGson.dailyForecast.Forecast forecast = forecasts.get(daysBetween.intValue());
                System.out.println("Weather criteria: " + weatherCriteria + " and forecast weather: " + forecast.getWeather().get(0).getMain());
                return forecast.getWeather().get(0).getMain().equals(weatherCriteria);


            } else {
                List<fi.tuni.sd.tourismPlanner.utils.openWeatherGson.climaticForecast.Forecast> forecasts = climaticForecastData.getList();
                fi.tuni.sd.tourismPlanner.utils.openWeatherGson.climaticForecast.Forecast forecast = forecasts.get(daysBetween.intValue());
                System.out.println("Weather criteria: " + weatherCriteria + " and forecast weather: " + forecast.getWeather().get(0).getMain());
                return forecast.getWeather().get(0).getMain().equals(weatherCriteria);
            }
        }
    }

    /**
     * Gets weather data for a single date
     *
     * @param date   date in UNIX timestamp format
     * @param lat    latitude for the location
     * @param lon    longitude for the location
     * @param apiKey API key for the weather service
     * @return WeatherData for the date as a TreeMap, where the key is the date in yyyy-mm-dd format and the value is a list of WeatherData objects
     */
    public TreeMap<String, ArrayList<WeatherData>> getWeatherDataForDate(Long date, Double lat, Double lon, String apiKey) {
        System.out.println("Getting weather data for date");

        boolean weatherDataSuccesfullyUpadated = weatherService.updateWeatherData(lat, lon, apiKey);

        if (!weatherDataSuccesfullyUpadated) {
            System.out.println("Something went wrong in fetching the weather data.");
            return null;
        }
        System.out.println("Updating weather data succeeded");

        HourlyForecastData hourlyForecastData = weatherService.getHourlyForecastData(lat, lon);
        System.out.println("Getting hourly forecast data succeeded");

        ThreeHourForecastData threeHourForecastData = weatherService.getThreeHourForecastData(lat, lon);
        System.out.println("Getting three hour forecast data succeeded");

        ClimaticForecastData climaticForecastData = weatherService.getClimaticForecastData(lat, lon);
        System.out.println("Getting climatic forecast data succeeded");

        DailyForecastData dailyForecastData = weatherService.getDailyForecastData(lat, lon);
        System.out.println("Getting daily forecast data succeeded");

        LocalDate localDate = Instant.ofEpochSecond(date).atZone(ZoneId.systemDefault()).toLocalDate();

        ArrayList<WeatherData> forecastsForDay = new ArrayList<>();

        Long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), localDate);

        handleForecasts(daysBetween.intValue(), localDate, forecastsForDay, hourlyForecastData, threeHourForecastData, dailyForecastData, climaticForecastData);

        TreeMap<String, ArrayList<WeatherData>> weatherData = new TreeMap<>();
        DateTimeFormatter outputFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        String formattedDate = localDate.format(outputFormatter);

        weatherData.put(formattedDate, forecastsForDay);

        return weatherData;
    }

    /**
     * Filters forecasts by date
     *
     * @param <T>           Forecast objects as a list
     * @param forecasts     List of forecasts
     * @param localDate     Date to filter the forecasts by
     * @param getDtFunction method reference to get the date from the forecast object
     * @return List<T> of forecasts for the given date
     */
    private <T> List<T> filterForecastsByDate(List<T> forecasts, LocalDate localDate, Function<T, Long> getDtFunction) {
        return forecasts.stream()
                .filter(forecast -> {
                    LocalDateTime forecastDateTime = LocalDateTime
                            .ofInstant(Instant.ofEpochSecond(getDtFunction.apply(forecast)), ZoneId.systemDefault());
                    return forecastDateTime.toLocalDate().equals(localDate);
                })
                .collect(Collectors.toList());
    }

    /**
     * Processes forecasts and creates WeatherData objects
     *
     * @param forecasts                 Forecasts as a list
     * @param createWeatherDataFunction Applicable method reference to create WeatherData object
     * @param forecastsForDay           List to collect the created WeatherData objects
     */
    private <T> void processForecasts(List<T> forecasts, Function<T, WeatherData> createWeatherDataFunction, List<WeatherData> forecastsForDay) {
        forecasts.forEach(forecast -> {
            WeatherData weatherData = createWeatherDataFunction.apply(forecast);
            forecastsForDay.add(weatherData);
        });
    }

    /**
     * Creates WeatherData -objects based on the forecast data and adds these into list. Based on how many days in the future the forecast is,
     * the data used to create the WeatherData objects is selected accordingly.
     *
     * @param daysBetween           Number of days between the current date and the forecast date
     * @param localDate             Date of the forecast
     * @param forecastsForDay       List to collect the created WeatherData objects
     * @param hourlyForecastData    Hourly forecast data
     * @param threeHourForecastData Three hour forecast data
     * @param dailyForecastData     Daily forecast data
     * @param climaticForecastData  Climatic forecast data
     */
    private void handleForecasts(int daysBetween, LocalDate localDate, List<WeatherData> forecastsForDay,
                                 HourlyForecastData hourlyForecastData, ThreeHourForecastData threeHourForecastData,
                                 DailyForecastData dailyForecastData, ClimaticForecastData climaticForecastData) {

        if (daysBetween <= 3) {
            List<fi.tuni.sd.tourismPlanner.utils.openWeatherGson.hourlyForecast.Forecast> weatherDataForDate
                    = filterForecastsByDate(hourlyForecastData.getList(), localDate, fi.tuni.sd.tourismPlanner.utils.openWeatherGson.hourlyForecast.Forecast::getDt);

            processForecasts(weatherDataForDate,
                    forecast -> createHourlyWeatherdataClass("hourly", forecast), forecastsForDay);

        } else if (daysBetween <= 4) {
            List<fi.tuni.sd.tourismPlanner.utils.openWeatherGson.threeHourForecast.Forecast> weatherDataForDate
                    = filterForecastsByDate(
                    threeHourForecastData.getList(),
                    localDate, fi.tuni.sd.tourismPlanner.utils.openWeatherGson.threeHourForecast.Forecast::getDt);
            processForecasts(weatherDataForDate,
                    forecast -> createThreeHourWeatherDataClass("threeHourly", forecast),
                    forecastsForDay);

        } else if (daysBetween <= 15) {
            List<fi.tuni.sd.tourismPlanner.utils.openWeatherGson.dailyForecast.Forecast> weatherDataForDate
                    = filterForecastsByDate(
                    dailyForecastData.getList(), localDate,
                    fi.tuni.sd.tourismPlanner.utils.openWeatherGson.dailyForecast.Forecast::getDt);
            processForecasts(weatherDataForDate,
                    forecast -> createDailyWeatherdataClass("daily", forecast), forecastsForDay);

        } else if (daysBetween <= 29) {
            List<fi.tuni.sd.tourismPlanner.utils.openWeatherGson.climaticForecast.Forecast> weatherDataForDate
                    = filterForecastsByDate(
                    climaticForecastData.getList(), localDate,
                    fi.tuni.sd.tourismPlanner.utils.openWeatherGson.climaticForecast.Forecast::getDt);
            processForecasts(weatherDataForDate,
                    forecast -> createClimaticWeatherDataClass("climatic", forecast),
                    forecastsForDay);
        } else {
            System.out.println(
                    "Unfortunately we can't provide weather data for dates that are more than 29 days in the future.");
        }
    }

    /**
     * Creates WeatherData object for hourly forecast
     *
     * @param weatherDataInterval Description of the weather data interval (hourly, threeHourly, daily, climatic)
     * @param forecast            forecast object used to create the WeatherData object
     * @return WeatherData object
     */
    private WeatherData createHourlyWeatherdataClass(String weatherDataInterval,
                                                     fi.tuni.sd.tourismPlanner.utils.openWeatherGson.hourlyForecast.Forecast forecast) {

        WeatherData weatherDataForHour = new WeatherData("hourly");

        weatherDataForHour.setDt(forecast.getDt());
        weatherDataForHour.setTextDateTime(forecast.getDt_txt());

        weatherDataForHour.setTemp(forecast.getMain().getTemp());
        weatherDataForHour.setFeelsLike(forecast.getMain().getFeels_like());
        weatherDataForHour.setTempMin(forecast.getMain().getTemp_min());
        weatherDataForHour.setTempMax(forecast.getMain().getTemp_max());

        weatherDataForHour.setPressure(forecast.getMain().getPressure());
        weatherDataForHour.setHumidity(forecast.getMain().getHumidity());

        if (forecast.getRain() != null) {
            weatherDataForHour.setRain(forecast.getRain().get_1h());
        }
        weatherDataForHour.setPop(forecast.getPop());

        weatherDataForHour.setSpeed(forecast.getWind().getSpeed());
        weatherDataForHour.setDeg(forecast.getWind().getDeg());
        weatherDataForHour.setClouds(forecast.getClouds().getAll());
        weatherDataForHour.setGust(forecast.getWind().getGust());

        weatherDataForHour.setWeatherId(forecast.getWeather().get(0).getId());
        weatherDataForHour.setWeatherDescription(forecast.getWeather().get(0).getDescription());
        weatherDataForHour.setWeatherMain(forecast.getWeather().get(0).getMain());
        weatherDataForHour.setWeatherIcon(forecast.getWeather().get(0).getIcon());

        return weatherDataForHour;
    }

    /**
     * Creates WeatherData object for three hour forecast
     *
     * @param weatherDataInterval Description of the weather data interval (hourly, threeHourly, daily, climatic)
     * @param forecast            forecast object used to create the WeatherData object
     * @return WeatherData object
     */
    private WeatherData createThreeHourWeatherDataClass(String weatherDataInterval,
                                                        fi.tuni.sd.tourismPlanner.utils.openWeatherGson.threeHourForecast.Forecast forecast) {

        WeatherData weatherDataFor3Hours = new WeatherData("threeHourly");

        weatherDataFor3Hours.setDt(forecast.getDt());
        weatherDataFor3Hours.setTextDateTime(forecast.getDt_txt());

        weatherDataFor3Hours.setTemp(forecast.getMain().getTemp());
        weatherDataFor3Hours.setFeelsLike(forecast.getMain().getFeels_like());
        weatherDataFor3Hours.setTempMin(forecast.getMain().getTemp_min());
        weatherDataFor3Hours.setTempMax(forecast.getMain().getTemp_max());

        weatherDataFor3Hours.setPressure(forecast.getMain().getPressure());
        weatherDataFor3Hours.setHumidity(forecast.getMain().getHumidity());
        if (forecast.getRain() != null) {
            weatherDataFor3Hours.setRain(forecast.getRain().get3h());
        }
        weatherDataFor3Hours.setPop(forecast.getPop());

        weatherDataFor3Hours.setSpeed(forecast.getWind().getSpeed());
        weatherDataFor3Hours.setDeg(forecast.getWind().getDeg());
        weatherDataFor3Hours.setClouds(forecast.getClouds().getAll());
        weatherDataFor3Hours.setGust(forecast.getWind().getGust());

        weatherDataFor3Hours.setWeatherId(forecast.getWeather().get(0).getId());
        weatherDataFor3Hours.setWeatherDescription(forecast.getWeather().get(0).getDescription());
        weatherDataFor3Hours.setWeatherMain(forecast.getWeather().get(0).getMain());
        weatherDataFor3Hours.setWeatherIcon(forecast.getWeather().get(0).getIcon());

        return weatherDataFor3Hours;
    }

    /**
     * Creates WeatherData object for daily forecast
     *
     * @param weatherDataInterval Description of the weather data interval (hourly, threeHourly, daily, climatic)
     * @param forecast            forecast object used to create the WeatherData object
     * @return WeatherData object
     */
    private WeatherData createDailyWeatherdataClass(String weatherDataInterval,
                                                    fi.tuni.sd.tourismPlanner.utils.openWeatherGson.dailyForecast.Forecast forecast) {
        WeatherData weatherDataForDay = new WeatherData("daily");

        weatherDataForDay.setDt(forecast.getDt());

        weatherDataForDay.setTempMorn(forecast.getTemp().getMorn());
        weatherDataForDay.setTempDay(forecast.getTemp().getDay());
        weatherDataForDay.setTempEve(forecast.getTemp().getEve());
        weatherDataForDay.setTempNight(forecast.getTemp().getNight());

        weatherDataForDay.setTempMin(forecast.getTemp().getMin());
        weatherDataForDay.setTempMax(forecast.getTemp().getMax());

        weatherDataForDay.setFeelsLikeMorn(forecast.getFeelsLike().getMorn());
        weatherDataForDay.setFeelsLikeDay(forecast.getFeelsLike().getDay());
        weatherDataForDay.setFeelsLikeEve(forecast.getFeelsLike().getEve());
        weatherDataForDay.setFeelsLikeNight(forecast.getFeelsLike().getNight());

        weatherDataForDay.setPressure(forecast.getPressure());
        weatherDataForDay.setHumidity(forecast.getHumidity());
        weatherDataForDay.setRain(forecast.getRain());
        weatherDataForDay.setPop(forecast.getPop());

        weatherDataForDay.setSpeed(forecast.getSpeed());
        weatherDataForDay.setDeg(forecast.getDeg());
        weatherDataForDay.setClouds(forecast.getClouds());
        weatherDataForDay.setGust(forecast.getGust());

        weatherDataForDay.setWeatherId(forecast.getWeather().get(0).getId());
        weatherDataForDay.setWeatherDescription(forecast.getWeather().get(0).getDescription());
        weatherDataForDay.setWeatherMain(forecast.getWeather().get(0).getMain());
        weatherDataForDay.setWeatherIcon(forecast.getWeather().get(0).getIcon());

        return weatherDataForDay;
    }

    /**
     * Creates WeatherData object for climatic forecast
     *
     * @param weatherDataInterval Description of the weather data interval (hourly, threeHourly, daily, climatic)
     * @param forecast            forecast object used to create the WeatherData object
     * @return WeatherData object
     */
    private WeatherData createClimaticWeatherDataClass(String weatherDataInterval,
                                                       fi.tuni.sd.tourismPlanner.utils.openWeatherGson.climaticForecast.Forecast forecast) {

        WeatherData weatherDataForDay = new WeatherData("climatic");

        weatherDataForDay.setDt(forecast.getDt());

        weatherDataForDay.setTempMorn(forecast.getTemp().getMorn());
        weatherDataForDay.setTempDay(forecast.getTemp().getDay());
        weatherDataForDay.setTempEve(forecast.getTemp().getEve());
        weatherDataForDay.setTempNight(forecast.getTemp().getNight());

        weatherDataForDay.setFeelsLikeMorn(forecast.getFeels_like().getMorn());
        weatherDataForDay.setFeelsLikeDay(forecast.getFeels_like().getDay());
        weatherDataForDay.setFeelsLikeEve(forecast.getFeels_like().getEve());
        weatherDataForDay.setFeelsLikeNight(forecast.getFeels_like().getNight());

        weatherDataForDay.setTempMin(forecast.getTemp().getMin());
        weatherDataForDay.setTempMax(forecast.getTemp().getMax());

        weatherDataForDay.setPressure(forecast.getPressure());
        weatherDataForDay.setHumidity(forecast.getHumidity());

        weatherDataForDay.setRain(forecast.getRain());

        weatherDataForDay.setSpeed(forecast.getSpeed());
        weatherDataForDay.setDeg(forecast.getDeg());
        weatherDataForDay.setClouds(forecast.getClouds());

        weatherDataForDay.setWeatherId(forecast.getWeather().get(0).getId());
        weatherDataForDay.setWeatherDescription(forecast.getWeather().get(0).getDescription());
        weatherDataForDay.setWeatherMain(forecast.getWeather().get(0).getMain());
        weatherDataForDay.setWeatherIcon(forecast.getWeather().get(0).getIcon());

        return weatherDataForDay;
    }
}
