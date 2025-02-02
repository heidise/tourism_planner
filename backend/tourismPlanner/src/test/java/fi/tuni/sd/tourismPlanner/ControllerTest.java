package fi.tuni.sd.tourismPlanner;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ControllerTest {

    Controller controller = new Controller();

    @Test
    void testGetEvents() {
        String city = "London,Berlin,Paris";
        String classificationName = "Music";
        String weather = "All";
        String startDateTime = "2024-12-15T00:00:00Z";
        String endDateTime = "2024-12-1T23:59:59Z";

        String event_list = controller.getEvents(city, classificationName, weather, startDateTime, endDateTime);
        Assertions.assertThat(event_list).isNotNull();
        Assertions.assertThat(event_list).isNotEqualTo("[]");
    }

    @Test
    void testGetEvent() {
        String id = "1AgZkvfGkdS197H";

        String event_details = controller.getEvent(id);
        Assertions.assertThat(event_details).isNotNull();
        // Check correct event is fetched
        Assertions.assertThat(event_details).contains("ABBA Voyage");
        // Check forecast object likely contains something
        Assertions.assertThat(event_details).contains("weatherDataInterval");
    }
}