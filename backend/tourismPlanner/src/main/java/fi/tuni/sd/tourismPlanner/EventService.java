package fi.tuni.sd.tourismPlanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import fi.tuni.sd.tourismPlanner.utils.ticketmasterGson.Event;
import fi.tuni.sd.tourismPlanner.utils.ticketmasterGson.EventParameters;
import fi.tuni.sd.tourismPlanner.utils.ticketmasterGson.PriceRange;
import fi.tuni.sd.tourismPlanner.utils.ticketmasterGson.SimpleEvent;

/**
 * Service class for fetching event data from ticketmaster discovery API
 */

public class EventService {

    private static final String BASE_URL = "https://app.ticketmaster.com/discovery/v2/events";

    /**
     * Fetches events with matching parameters from ticketmaster API.
     *
     * @param params EventParameters, record containing city, category, startTime and endTime.
     * @param apiKey String, apiKey for ticketmaster API.
     * @return List<SimpleEvent>, list of SimpleEvent objects matching criteria.
     */
    public List<SimpleEvent> getEvents(EventParameters params, String apiKey) {
        List<SimpleEvent> simpleEvents = new ArrayList<>();

        String startTime = convertToIsoOrKeepOriginal(params.startTime());
        String endTime = convertToIsoOrKeepOriginal(params.endTime());

        String urlString = BASE_URL + "?apikey=" + apiKey + "&locale=*&startDateTime=" + startTime + "&endDateTime=" + endTime + "&city=" + params.city();
        if (!params.type().isEmpty()) {
            urlString += "&classificationName=" + params.type();
        }
        try {
            JsonNode rootNode = urlToJson(urlString);
            JsonNode eventsNode = rootNode.path("_embedded").path("events");
            for (JsonNode eventNode : eventsNode) {
                simpleEvents.add(jsonNodeToSimpleEvent(eventNode));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return simpleEvents;
    }

    /**
     * Fetches details about single event from ticketmaster API.
     *
     * @param id     String, event id.
     * @param apiKey String, apiKey for ticketmaster API.
     * @return Event, object containing information about specified event.
     */
    public Event getEventById(String id, String apiKey) {
        String urlString = BASE_URL + "/" + id + "?apikey=" + apiKey;
        try {
            JsonNode rootNode = urlToJson(urlString);
            return jsonNodeToEvent(rootNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Converts a list of SimpleEvents into a json string.
     *
     * @param events List<SimpleEvent>, List of SimpleEvent objects to be converted.
     * @return String, json containing events.
     */
    public String convertSimpleEventsToJsonString(List<SimpleEvent> events) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return mapper.writeValueAsString(events);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts event details into a json string.
     *
     * @param event Event, object containing event data to be converted .
     * @return String, json event details.
     */
    public String convertEventToJsonString(Event event) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return mapper.writeValueAsString(event);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts a Unix timestamp to ISO 8601 format, or returns the input if it's not a timestamp.
     *
     * @param dateTime String, input date-time or Unix timestamp.
     * @return String, ISO 8601 formatted date-time or original input.
     */
    private String convertToIsoOrKeepOriginal(String dateTime) {
        try {
            long unixTime = Long.parseLong(dateTime);
            return Instant.ofEpochSecond(unixTime)
                    .atOffset(ZoneOffset.UTC)
                    .format(DateTimeFormatter.ISO_INSTANT);
        } catch (NumberFormatException e) {
            return dateTime;
        }
    }

    /**
     * Sends a GET request to the specified URL and parses the JSON response.
     *
     * @param urlString String, the URL to send the request to.
     * @return JsonNode, parsed JSON response.
     * @throws IOException if there is an issue with the connection or response.
     */
    private JsonNode urlToJson(String urlString) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        System.out.println(urlString);
        if (conn.getResponseCode() == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(response.toString());
        } else {
            System.out.println("Error with urlToJson: " + conn.getResponseCode());
            throw new IOException();
        }
    }

    /**
     * Converts a JsonNode representing an event into a SimpleEvent object.
     *
     * @param node JsonNode, JSON representation of the event.
     * @return SimpleEvent, a simplified event object.
     */
    private SimpleEvent jsonNodeToSimpleEvent(JsonNode node) {
        String id = node.path("id").asText();
        String name = node.path("name").asText();
        String city = node.path("_embedded").path("venues").get(0).path("city").path("name").asText();
        String eventStartTime = node.path("dates").path("start").path("dateTime").asText();
        if (eventStartTime.isEmpty()) {
            eventStartTime = node.path("dates").path("start").path("localDate").asText();
        }
        String location = node.path("_embedded").path("venues").get(0).path("name").asText();
        String imageUrl = node.path("images").get(0).path("url").asText();

        return new SimpleEvent(id, name, city, eventStartTime, location, imageUrl);
    }

    /**
     * Converts a JsonNode representing an event into a detailed Event object.
     *
     * @param node JsonNode, JSON representation of the event.
     * @return Event, a detailed event object.
     */
    private Event jsonNodeToEvent(JsonNode node) {
        SimpleEvent simpleEvent = jsonNodeToSimpleEvent(node);
        String address = node.path("_embedded").path("venues").get(0).path("address").path("line1").asText();
        String saleStart = node.path("sales").path("public").path("startDateTime").asText();
        String saleEnd = node.path("sales").path("public").path("endDateTime").asText();
        String url = node.path("url").asText();
        String info = node.path("info").asText();
        String description = node.path("promoter").path("description").asText();
        List<PriceRange> priceRanges = new ArrayList<>();
        for (JsonNode priceNode : node.path("priceRanges")) {
            String type = priceNode.path("type").asText();
            String currency = priceNode.path("currency").asText();
            Double min = Double.valueOf(priceNode.path("min").asText());
            Double max = Double.valueOf(priceNode.path("max").asText());
            priceRanges.add(new PriceRange(type, currency, min, max));
        }
        return new Event(simpleEvent, address, saleStart, saleEnd, priceRanges, url, info, description);
    }
}