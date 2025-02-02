
package fi.tuni.sd.tourismPlanner;

//import fi.tuni.sd.tourismPlanner.WeatherService;
import fi.tuni.sd.tourismPlanner.utils.openWeatherGson.dailyForecast.DailyForecastData;
import fi.tuni.sd.tourismPlanner.utils.openWeatherGson.hourlyForecast.HourlyForecastData;
import fi.tuni.sd.tourismPlanner.utils.openWeatherGson.threeHourForecast.ThreeHourForecastData;
import org.junit.jupiter.api.Test;
import com.google.gson.Gson;
import static org.junit.jupiter.api.Assertions.*;


public class WeatherServiceTest {
    private WeatherService weatherService;

    @Test
    public void testGetDailyForecastData() throws Exception {
        Gson gson = new Gson();
        String jsonResponse = "{ \"city\": { \"id\": 3163858, \"name\": \"Zocca\", \"coord\": { \"lon\": 10.99, \"lat\": 44.34 }, "
            + "\"country\": \"IT\", \"population\": 4593, \"timezone\": 7200 }, \"cod\": \"200\", \"message\": 0.0582563, \"cnt\": 7, "
            + "\"list\": [ { \"dt\": 1661857200, \"sunrise\": 1661834187, \"sunset\": 1661882248, \"temp\": { \"day\": 299.66, "
            + "\"min\": 288.93, \"max\": 299.66, \"night\": 290.31, \"eve\": 297.16, \"morn\": 288.93 }, \"feels_like\": { \"day\": "
            + "299.66, \"night\": 290.3, \"eve\": 297.1, \"morn\": 288.73 }, \"pressure\": 1017, \"humidity\": 44, \"weather\": [ "
            + "{ \"id\": 500, \"main\": \"Rain\", \"description\": \"light rain\", \"icon\": \"10d\" } ], \"speed\": 2.7, \"deg\": 209, "
            + "\"gust\": 3.58, \"clouds\": 53, \"pop\": 0.7, \"rain\": 2.51 }, { \"dt\": 1661943600, \"sunrise\": 1661920656, "
            + "\"sunset\": 1661968542, \"temp\": { \"day\": 295.76, \"min\": 287.73, \"max\": 295.76, \"night\": 289.37, \"eve\": "
            + "292.76, \"morn\": 287.73 }, \"feels_like\": { \"day\": 295.64, \"night\": 289.45, \"eve\": 292.97, \"morn\": 287.59 }, "
            + "\"pressure\": 1014, \"humidity\": 60, \"weather\": [ { \"id\": 500, \"main\": \"Rain\", \"description\": \"light rain\", "
            + "\"icon\": \"10d\" } ], \"speed\": 2.29, \"deg\": 215, \"gust\": 3.27, \"clouds\": 66, \"pop\": 0.82, \"rain\": 5.32 }, "
            + "{ \"dt\": 1662030000, \"sunrise\": 1662007126, \"sunset\": 1662054835, \"temp\": { \"day\": 293.38, \"min\": 287.06, "
            + "\"max\": 293.38, \"night\": 287.06, \"eve\": 289.01, \"morn\": 287.84 }, \"feels_like\": { \"day\": 293.31, \"night\": "
            + "287.01, \"eve\": 289.05, \"morn\": 287.85 }, \"pressure\": 1014, \"humidity\": 71, \"weather\": [ { \"id\": 500, "
            + "\"main\": \"Rain\", \"description\": \"light rain\", \"icon\": \"10d\" } ], \"speed\": 2.67, \"deg\": 60, \"gust\": 2.66, "
            + "\"clouds\": 97, \"pop\": 0.84, \"rain\": 4.49 } ] }";

        DailyForecastData data = gson.fromJson(jsonResponse, DailyForecastData.class);
        assertNotNull(data);
        
    }

    @Test
    public void testGetThreeHourForecastData() throws Exception {
        Gson gson = new Gson();

        String jsonResponse = "{ \"cod\": \"200\", \"message\": 0, \"cnt\": 40, \"list\": [ { \"dt\": 1661871600, \"main\": { \"temp\": 296.76, "
            + "\"feels_like\": 296.98, \"temp_min\": 296.76, \"temp_max\": 297.87, \"pressure\": 1015, \"sea_level\": 1015, "
            + "\"grnd_level\": 933, \"humidity\": 69, \"temp_kf\": -1.11 }, \"weather\": [ { \"id\": 500, \"main\": \"Rain\", "
            + "\"description\": \"light rain\", \"icon\": \"10d\" } ], \"clouds\": { \"all\": 100 }, \"wind\": { \"speed\": 0.62, "
            + "\"deg\": 349, \"gust\": 1.18 }, \"visibility\": 10000, \"pop\": 0.32, \"rain\": { \"3h\": 0.26 }, \"sys\": { \"pod\": "
            + "\"d\" }, \"dt_txt\": \"2022-08-30 15:00:00\" }, { \"dt\": 1661882400, \"main\": { \"temp\": 295.45, \"feels_like\": "
            + "295.59, \"temp_min\": 292.84, \"temp_max\": 295.45, \"pressure\": 1015, \"sea_level\": 1015, \"grnd_level\": 931, "
            + "\"humidity\": 71, \"temp_kf\": 2.61 }, \"weather\": [ { \"id\": 500, \"main\": \"Rain\", \"description\": \"light rain\", "
            + "\"icon\": \"10n\" } ], \"clouds\": { \"all\": 96 }, \"wind\": { \"speed\": 1.97, \"deg\": 157, \"gust\": 3.39 }, "
            + "\"visibility\": 10000, \"pop\": 0.33, \"rain\": { \"3h\": 0.57 }, \"sys\": { \"pod\": \"n\" }, \"dt_txt\": "
            + "\"2022-08-30 18:00:00\" }, { \"dt\": 1661893200, \"main\": { \"temp\": 292.46, \"feels_like\": 292.54, \"temp_min\": "
            + "290.31, \"temp_max\": 292.46, \"pressure\": 1015, \"sea_level\": 1015, \"grnd_level\": 931, \"humidity\": 80, "
            + "\"temp_kf\": 2.15 }, \"weather\": [ { \"id\": 500, \"main\": \"Rain\", \"description\": \"light rain\", \"icon\": "
            + "\"10n\" } ], \"clouds\": { \"all\": 68 }, \"wind\": { \"speed\": 2.66, \"deg\": 210, \"gust\": 3.58 }, \"visibility\": "
            + "10000, \"pop\": 0.7, \"rain\": { \"3h\": 0.49 }, \"sys\": { \"pod\": \"n\" }, \"dt_txt\": \"2022-08-30 21:00:00\" }, "
            + "{ \"dt\": 1662292800, \"main\": { \"temp\": 294.93, \"feels_like\": 294.83, \"temp_min\": 294.93, \"temp_max\": "
            + "294.93, \"pressure\": 1018, \"sea_level\": 1018, \"grnd_level\": 935, \"humidity\": 64, \"temp_kf\": 0 }, \"weather\": "
            + "[ { \"id\": 804, \"main\": \"Clouds\", \"description\": \"overcast clouds\", \"icon\": \"04d\" } ], \"clouds\": { "
            + "\"all\": 88 }, \"wind\": { \"speed\": 1.14, \"deg\": 17, \"gust\": 1.57 }, \"visibility\": 10000, \"pop\": 0, \"sys\": "
            + "{ \"pod\": \"d\" }, \"dt_txt\": \"2022-09-04 12:00:00\" } ], \"city\": { \"id\": 3163858, \"name\": \"Zocca\", \"coord\": "
            + "{ \"lat\": 44.34, \"lon\": 10.99 }, \"country\": \"IT\", \"population\": 4593, \"timezone\": 7200, \"sunrise\": "
            + "1661834187, \"sunset\": 1661882248 } }";

        ThreeHourForecastData data = gson.fromJson(jsonResponse, ThreeHourForecastData.class);
        
        //assertNotNull(data);
        assertEquals("200", data.getCod());
        assertEquals("overcast clouds", data.getList().get(3).getWeather().get(0).getDescription());
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetHourlyForecastData() throws Exception {
        Gson gson = new Gson();
        String jsonResponse = "{\"cod\":\"200\",\"message\":0,\"cnt\":96,\"list\":[{\"dt\":1661875200,\"main\":{\"temp\":296.34,"
            + "\"feels_like\":296.02,\"temp_min\":296.34,\"temp_max\":298.24,\"pressure\":1015,\"sea_level\":1015,\"grnd_level\":933,"
            + "\"humidity\":50,\"temp_kf\":-1.9},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":"
            + "\"10d\"}],\"clouds\":{\"all\":97},\"wind\":{\"speed\":1.06,\"deg\":66,\"gust\":2.16},\"visibility\":10000,\"pop\":0.32,"
            + "\"rain\":{\"1h\":0.13},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-08-30 16:00:00\"},{\"dt\":1661878800,\"main\":"
            + "{\"temp\":296.31,\"feels_like\":296.07,\"temp_min\":296.2,\"temp_max\":296.31,\"pressure\":1015,\"sea_level\":1015,"
            + "\"grnd_level\":932,\"humidity\":53,\"temp_kf\":0.11},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":"
            + "\"light rain\",\"icon\":\"10d\"}],\"clouds\":{\"all\":95},\"wind\":{\"speed\":1.58,\"deg\":103,\"gust\":3.52},"
            + "\"visibility\":10000,\"pop\":0.4,\"rain\":{\"1h\":0.24},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2022-08-30 17:00:00\"},"
            + "{\"dt\":1661882400,\"main\":{\"temp\":294.94,\"feels_like\":294.74,\"temp_min\":292.84,\"temp_max\":294.94,\"pressure\":"
            + "1015,\"sea_level\":1015,\"grnd_level\":931,\"humidity\":60,\"temp_kf\":2.1},\"weather\":[{\"id\":500,\"main\":\"Rain\","
            + "\"description\":\"light rain\",\"icon\":\"10n\"}],\"clouds\":{\"all\":93},\"wind\":{\"speed\":1.97,\"deg\":157,"
            + "\"gust\":3.39},\"visibility\":10000,\"pop\":0.33,\"rain\":{\"1h\":0.2},\"sys\":{\"pod\":\"n\"},\"dt_txt\":"
            + "\"2022-08-30 18:00:00\"},{\"dt\":1662217200,\"main\":{\"temp\":294.14,\"feels_like\":293.99,\"temp_min\":294.14,"
            + "\"temp_max\":294.14,\"pressure\":1014,\"sea_level\":1014,\"grnd_level\":931,\"humidity\":65,\"temp_kf\":0},\"weather\":"
            + "[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"clouds\":{\"all\":100},"
            + "\"wind\":{\"speed\":0.91,\"deg\":104,\"gust\":1.92},\"visibility\":10000,\"pop\":0.53,\"sys\":{\"pod\":\"d\"},"
            + "\"dt_txt\":\"2022-09-03 15:00:00\"}],\"city\":{\"id\":3163858,\"name\":\"Zocca\",\"coord\":{\"lat\":44.34,\"lon\":10.99},"
            + "\"country\":\"IT\",\"population\":4593,\"timezone\":7200,\"sunrise\":1661834187,\"sunset\":1661882248}}";

        HourlyForecastData data = gson.fromJson(jsonResponse, HourlyForecastData.class);
        assertNotNull(data);
        assertEquals(data.getCod(), "200");
    }
}
