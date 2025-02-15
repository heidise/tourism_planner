# Course Software Design: Group assignment

Tourism planner is a web application that combines weather data with data from events happening in the near future. The purpose of the app is to give the user an option to filter events based on event location, event category and user’s weather preferences but also give idea of how they should prepare for the event in clothing and otherwise. The project uses two open APIs: Ticketmaster API and OpenWeather API. The original idea for this application was generated by Chat GPT.

**Group assignment work division:**
- Heidi Seppi (frontend): Event page UI, retrieving and modifying data from backend, charts
- Aynur (frontend): Home page UI, retrieving and modifying data from backend, charts  
- Lauri (backend): Controller basic structure, EventService, geocoding and event related utility classes. 
- Ville (backend): WeatherService and WeatherController –classes and weather related utility classes. 

**See the whole project documentation under /docs folder.**

**Snippets of the home and event pages:**

<img src="https://github.com/heidise/tourism_planner/blob/main/docs/snippet1.PNG" width="700"/>

<img src="https://github.com/heidise/tourism_planner/blob/main/docs/snippet2.PNG" width="700"/>

---
**Running the program:**
Java version 21 or newer is required to run the application.

Run backend:
- `cd tourism_planner\backend\tourismPlanner`
- `mvn spring-boot:run`

When running controller listens on port 8080

Project needs API keys for the backend to work: please add your own API keys under backend/tourismPlanner/src/main/java/fi/tuni/sd/tourismPlanner/resources and replace ADD-WEATHER-API-KEY-HERE with your OpenWeather API key and ADD-EVENT-API-KEY-HERE with your Ticketmaster API key. API keys can be created in: https://openweathermap.org/api, https://developer.ticketmaster.com/products-and-docs/apis/discovery-api/v2/

---
Run frontend:
- `cd tourism_planner\frontend`
- `npm i`
- `npm run dev`

Frontend runs on localhost port 3000
