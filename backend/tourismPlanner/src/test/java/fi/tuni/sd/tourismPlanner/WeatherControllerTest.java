package fi.tuni.sd.tourismPlanner;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import fi.tuni.sd.tourismPlanner.utils.WeatherData;
import fi.tuni.sd.tourismPlanner.utils.openWeatherGson.climaticForecast.ClimaticForecastData;
import fi.tuni.sd.tourismPlanner.utils.openWeatherGson.dailyForecast.DailyForecastData;
import fi.tuni.sd.tourismPlanner.utils.openWeatherGson.threeHourForecast.ThreeHourForecastData;
import fi.tuni.sd.tourismPlanner.utils.openWeatherGson.hourlyForecast.HourlyForecastData;


public class WeatherControllerTest {

    @Mock
    private WeatherService weatherService;

    private WeatherController weatherController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        weatherController = new WeatherController(weatherService);
    }

    @Test
    public void testDateLocationMatchWeatherCriteria_WeatherCode() {
        DailyForecastData dailyForecastData = mock(DailyForecastData.class);
        ClimaticForecastData climaticForecastData = mock(ClimaticForecastData.class);

        when(weatherService.getDailyForecastData(anyDouble(), anyDouble())).thenReturn(dailyForecastData);
        when(weatherService.getClimaticForecastData(anyDouble(), anyDouble())).thenReturn(climaticForecastData);

        List<fi.tuni.sd.tourismPlanner.utils.openWeatherGson.dailyForecast.Forecast> dailyForecasts = new ArrayList<>();

        fi.tuni.sd.tourismPlanner.utils.openWeatherGson.dailyForecast.Forecast forecast = mock(fi.tuni.sd.tourismPlanner.utils.openWeatherGson.dailyForecast.Forecast.class);

        var weather = new fi.tuni.sd.tourismPlanner.utils.openWeatherGson.dailyForecast.Weather();
        weather.setMain("Clear");
        weather.setDescription("clear sky");
        ArrayList<fi.tuni.sd.tourismPlanner.utils.openWeatherGson.dailyForecast.Weather> weatherList = new ArrayList<>();
        weatherList.add(weather);

        when(forecast.getWeather()).thenReturn(weatherList);

        dailyForecasts.add(forecast);
        when(dailyForecastData.getList()).thenReturn(dailyForecasts);

        boolean result = weatherController.dateLocationMatchWeatherCriteria("Clear", Instant.now().getEpochSecond(), 60.192059, 24.945831);
        assertTrue(result);
    }

    @Test
    public void testGetWeatherDataForDate() {
        HourlyForecastData hourlyForecastData = mock(HourlyForecastData.class);
        ThreeHourForecastData threeHourForecastData = mock(ThreeHourForecastData.class);
        ClimaticForecastData climaticForecastData = mock(ClimaticForecastData.class);
        DailyForecastData dailyForecastData = mock(DailyForecastData.class);

        when(weatherService.updateWeatherData(anyDouble(), anyDouble(), anyString())).thenReturn(true);
        when(weatherService.getHourlyForecastData(anyDouble(), anyDouble())).thenReturn(hourlyForecastData);
        when(weatherService.getThreeHourForecastData(anyDouble(), anyDouble())).thenReturn(threeHourForecastData);
        when(weatherService.getClimaticForecastData(anyDouble(), anyDouble())).thenReturn(climaticForecastData);
        when(weatherService.getDailyForecastData(anyDouble(), anyDouble())).thenReturn(dailyForecastData);

        Long date = Instant.now().getEpochSecond();
        TreeMap<String, ArrayList<WeatherData>> result = weatherController.getWeatherDataForDate(date, 60.192059, 24.945831, "apiKey");

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}