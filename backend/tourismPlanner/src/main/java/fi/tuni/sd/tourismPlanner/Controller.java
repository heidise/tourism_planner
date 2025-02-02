package fi.tuni.sd.tourismPlanner;

import fi.tuni.sd.tourismPlanner.utils.WeatherData;
import fi.tuni.sd.tourismPlanner.utils.openWeatherGson.GeocodingCity;
import fi.tuni.sd.tourismPlanner.utils.ticketmasterGson.Event;
import fi.tuni.sd.tourismPlanner.utils.ticketmasterGson.EventParameters;
import fi.tuni.sd.tourismPlanner.utils.ticketmasterGson.SimpleEvent;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Controller class handling backend API endpoints.
 */
@RestController
public class Controller {

    private WeatherService weatherService;
    private WeatherController weatherController;
    private EventService eventService;
    private final String weatherApiKey;
    private final String eventApiKey;

    public Controller() {
        this.weatherService = new WeatherService();
        this.weatherController = new WeatherController(weatherService);
        this.eventService = new EventService();
        weatherApiKey = readApiKey("weatherApiKey");
        eventApiKey = readApiKey("eventApiKey");
    }

    /**
     * Endpoint to fetch a list of events based on location, category, weather, and time range.
     *
     * @param location      Comma-separated list of locations.
     * @param category      Comma-separated list of event categories.
     * @param weather       Desired weather conditions.
     * @param startDateTime Start time in ISO format.
     * @param endDateTime   End time in ISO format.
     * @return JSON string of events matching the criteria.
     */
    @GetMapping("/events") // List of available events
    public String getEvents(@RequestParam String location, @RequestParam String category, @RequestParam String weather, @RequestParam String startDateTime, @RequestParam String endDateTime) {
        // multiple Locations/Categories/Weathers separated by ','
        System.out.println("Request received: GET /events");

        ArrayList<String> locations = new ArrayList<>(Arrays.asList(location.split(",")));
        ArrayList<String> categories = new ArrayList<>(Arrays.asList(category.split(",")));
        ArrayList<String> weathers = new ArrayList<>(Arrays.asList(weather.split(",")));

        System.out.println("locations: " + locations);
        System.out.println("categories: " + categories);
        System.out.println("weathers: " + weathers);

        List<SimpleEvent> events = new ArrayList<>();
        for (String cityName : locations) {
            EventParameters params = new EventParameters(cityName, String.join(",", categories), startDateTime, endDateTime);

            GeocodingCity city = weatherService.getGeocodingCityData(cityName, weatherApiKey);
            if (city == null) {
                System.out.printf("%s was not found%n", cityName);
                continue;
            }

            Double lat = city.getLat();
            Double lon = city.getLon();

            // Update weather data for the location
            weatherService.updateWeatherData(lat, lon, weatherApiKey);

            List<SimpleEvent> tempEvents = eventService.getEvents(params, eventApiKey);

            // Filter events that match the weather criteria
            List<SimpleEvent> eventsThatMatchWeatherCriteria 
                = tempEvents.stream().filter(event ->
                    weatherController.dateLocationMatchWeatherCriteria(weathers.getFirst(),
                                                                    convertToUnix(event.getStartTime()),
                                                                    lat, lon))
                    .collect(Collectors.toList());

            System.out.printf(eventService.convertSimpleEventsToJsonString(eventsThatMatchWeatherCriteria));
            events.addAll(eventsThatMatchWeatherCriteria);
        }
        //return eventService.convertEventsToJsonString(events);
        return eventService.convertSimpleEventsToJsonString(events);

    }

    /**
     * Endpoint to fetch detailed information about a single event.
     *
     * @param id The ID of the event.
     * @return JSON string of the event details, including weather forecast.
     */
    @GetMapping("/event") // Event details and forecast
    public String getEvent(@RequestParam String id) {

        System.out.println("Request received: GET /event");
        Event event = eventService.getEventById(id, eventApiKey);
        event.setForecast(getForecast(event.getSimpleEvent().getCity(), event.getSimpleEvent().getStartTime()));
        return eventService.convertEventToJsonString(event);
    }

    /**
     * Fetches the weather forecast for a specific location and time.
     *
     * @param location The name of the location.
     * @param dateTime The date and time in ISO format.
     * @return A TreeMap containing weather data, or null if the location is not found.
     */
    private TreeMap<String, ArrayList<WeatherData>> getForecast(String location, String dateTime) {
        GeocodingCity city = weatherService.getGeocodingCityData(location, weatherApiKey);
        if (city == null) {
            System.out.printf("%s was not found%n", location);
            return null;
        }

        Double lat = city.getLat();
        Double lon = city.getLon();

        Long unixTime = convertToUnix(dateTime);

        System.out.println("Forecast for: " + location + " at " + dateTime);


        return weatherController.getWeatherDataForDate(unixTime, lat, lon, weatherApiKey);
    }

    /**
     * Converts a date-time string to a UNIX timestamp.
     *
     * @param dateTime The date-time string in ISO or other formats.
     * @return UNIX timestamp as a long.
     */
    private long convertToUnix(String dateTime) {
        try {
            // Try to parse as ISO date-time
            Long unixTime = convertIsoToUnix(dateTime);
            return unixTime;
        } catch (DateTimeParseException e) {
            System.out.println("Failed to parse as ISO date-time: " + e.getMessage());
            System.out.println("Attempting conersion from format yyyy-MM-dd");
            try {
                // Try to parse as UTC date
                Long unixTime = convertDateToUnix(dateTime);
                return unixTime;
            } catch (DateTimeParseException ex) {
                System.out.println("Failed to parse as UTC date: " + ex.getMessage());
                throw new IllegalArgumentException("Invalid date format: " + dateTime);
            }
        }
    }

    /**
     * Converts an ISO date-time string to a UNIX timestamp.
     *
     * @param isoDateTime The ISO date-time string.
     * @return UNIX timestamp as a long.
     */
    private long convertIsoToUnix(String isoDateTime) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(isoDateTime, DateTimeFormatter.ISO_DATE_TIME);
        Instant instant = zonedDateTime.toInstant();
        return instant.getEpochSecond();
    }

    /**
     * Converts a date string in yyyy-MM-dd format to a UNIX timestamp.
     *
     * @param date The date string.
     * @return UNIX timestamp as a long.
     */
    private static long convertDateToUnix(String date) {
        LocalDate localDate = LocalDate.parse(date);
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.of("UTC"));
        Instant instant = zonedDateTime.toInstant();
        return instant.getEpochSecond();
    }

    /**
     * Reads an API key from a file.
     *
     * @param apiKey The name of the file containing the API key.
     * @return The API key as a String, or a default message if not found.
     */
    private String readApiKey(String apiKey) {
        try (BufferedReader reader = new BufferedReader(new FileReader(String.format("src/main/resources/%s", apiKey)))) {
            String line = reader.readLine();  // Read the first line from the file
            if (line != null) {
                return line;
            } else {
                System.out.println("The file is empty.");
            }
        } catch (FileNotFoundException e) {
            System.out.printf("File not found, create file named %s to src/main/resources", apiKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Api key not found";
    }
}
