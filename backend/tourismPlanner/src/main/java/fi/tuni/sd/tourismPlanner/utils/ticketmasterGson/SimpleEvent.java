package fi.tuni.sd.tourismPlanner.utils.ticketmasterGson;

public class SimpleEvent {
    private String id;
    private String name;
    private String city;
    private String startTime;
    private String location;
    private String imageUrl;

    // Constructors
    public SimpleEvent(String id, String name, String city, String startTime, String location, String imageUrl) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.startTime = startTime;
        this.location = location;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
