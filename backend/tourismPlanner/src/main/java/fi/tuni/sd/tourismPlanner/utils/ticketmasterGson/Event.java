package fi.tuni.sd.tourismPlanner.utils.ticketmasterGson;

import fi.tuni.sd.tourismPlanner.utils.WeatherData;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Event {
    private SimpleEvent simpleEvent;
    private String address;
    private String saleStart;
    private String saleEnd;
    private List<PriceRange> priceRanges;
    private String url;
    private String info;
    private String description;
    private TreeMap<String, ArrayList<WeatherData>> forecast;

    public Event(SimpleEvent simpleEvent, String address, String saleStart, String saleEnd, List<PriceRange> priceRanges, String url, String info, String description) {
        this.simpleEvent = simpleEvent;
        this.address = address;
        this.saleStart = saleStart;
        this.saleEnd = saleEnd;
        this.priceRanges = priceRanges;
        this.url = url;
        this.info = info;
        this.description = description;
    }

    public SimpleEvent getSimpleEvent() {
        return simpleEvent;
    }

    public void setSimpleEvent(SimpleEvent simpleEvent) {
        this.simpleEvent = simpleEvent;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSaleStart() {
        return saleStart;
    }

    public void setSaleStart(String saleStart) {
        this.saleStart = saleStart;
    }

    public String getSaleEnd() {
        return saleEnd;
    }

    public void setSaleEnd(String saleEnd) {
        this.saleEnd = saleEnd;
    }

    public List<PriceRange> getPriceRanges() {
        return priceRanges;
    }

    public void setPriceRanges(List<PriceRange> priceRanges) {
        this.priceRanges = priceRanges;
    }

    public TreeMap<String, ArrayList<WeatherData>> getForecast() {
        return forecast;
    }

    public void setForecast(TreeMap<String, ArrayList<WeatherData>> forecast) {
        this.forecast = forecast;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
