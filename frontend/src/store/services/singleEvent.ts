import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';

interface singleEvent {
  simpleEvent: {
    id: string;
    name: string;
    city: string;
    startTime: string;
    location: string;
    imageUrl: string;
  };
  address: string;
  url: string;
  info: string;
  description: string;
  forecast: Record<string, any[]>;
}

interface GetSingleEventParams {
  id: string;
}

export const singleEventApi = createApi({
  reducerPath: 'singleEventApi',
  baseQuery: fetchBaseQuery({ baseUrl: '/api' }),
  endpoints: (builder) => ({
    getSingleEvent: builder.query<singleEvent, GetSingleEventParams>({
      query: ({ id }) => ({
        url: '/event',
        params: { id },
      }),
    }),

    getChart: builder.query({
      queryFn: async (
        {
          id,
          field,
        }: { id: string; field: keyof singleEvent['forecast'][string][0] },
        _queryApi,
        _extraOptions,
        fetchWithBQ
      ) => {
        // Fetch the event details
        const response = await fetchWithBQ({ url: '/event', params: { id } });

        if (response.error) {
          return { error: response.error };
        }

        const event = response.data as singleEvent;

        if (!event || !event.forecast) {
          return { data: [] }; // Return empty series if no forecast data
        }

        const forecastArray = Object.keys(event.forecast).flatMap((date) =>
          Array.isArray(event.forecast[date]) ? event.forecast[date] : []
        );

        if (forecastArray.length == 0) {
          return { data: [] };
        }

        // Climatic or daily weather data chart handling.
        else if (forecastArray !== null && forecastArray[0]['weatherDataInterval'] === 'climatic' ||
          forecastArray[0]['weatherDataInterval'] === 'daily'
        ) {
          let forecastData = forecastArray[0];
          if (forecastData['dt'] === 'null') {
            return { data: [] };
          }

          // Estimated times for morning, day, evening and night.
          const morningTime = new Date(forecastData['dt']).setUTCHours(9).valueOf();
          const dayTime = new Date(forecastData['dt']).setUTCHours(13).valueOf();
          const eveningTime = new Date(forecastData['dt']).setUTCHours(17).valueOf();
          const nightTime = new Date(forecastData['dt']).setUTCHours(22).valueOf();

          let morning;
          let day;
          let evening;
          let night;

          // Weather doesn't have temp, only temps for morning, day, evening and night.
          if (field === 'temp') {
            // Kelvin to Celsius conversion
            morning = forecastData['tempMorn'];
            day = forecastData['tempDay'];
            evening = forecastData['tempEve'];
            night = forecastData['tempNight'];

          }
          // Weather doesn't have feelsLike, only feelsLike temps for morning, day, evening and night.
          else if (field === 'feelsLike') {
            // Kelvin to Celsius conversion
            morning = forecastData['feelsLikeMorn']; 
            day = forecastData['feelsLikeDay'];
            evening = forecastData['feelsLikeEve'];
            night = forecastData['feelsLikeNight'];
          }

          else if (field === 'speed' || field === 'rain') {
            if (forecastData[field] === 'null') {
              return { data: [] };
            }

            const series = [
              {
                name: String(field),
                data: [
                  {
                    y: forecastData[field],
                    x: forecastData['dt'] * 1000,
                  }
                ]
              }
            ];
            return { data: series };
          }

          if ((morning || day || evening || night) === null
            || (morningTime || dayTime || eveningTime || nightTime) === null) {
            return { data: [] };
          }

          const series = [
            {
              name: String(field),
              data: [
                {
                  x: morningTime,
                  y: morning - 273.15,
                },
                {
                  x: dayTime,
                  y: day - 273.15,
                },
                {
                  x: eveningTime,
                  y: evening - 273.15,
                },
                {
                  x: nightTime,
                  y: night - 273.15,
                },
              ],
            }
          ];

          return { data: series };
        }

        // Hourly weather data chart handling.
        else { 
          // Process the forecast data into series format
          const series = [
            {
              name: String(field), // Ensure name is string
              data: Object.keys(event.forecast)
                .flatMap((date) =>
                  Array.isArray(event.forecast[date]) ? event.forecast[date] : []
                )
                .map((entry: any) => {
                  let yValue = entry[field];

                  // Convert temperature from Kelvin to Celsius if the field is "temp"
                  if (
                    (field === 'temp' || field === 'feelsLike') &&
                    yValue !== undefined &&
                    yValue !== null
                  ) {
                    yValue = (yValue - 273.15); // Kelvin to Celsius conversion
                  }

                  return {
                    x: entry?.dt ? entry.dt * 1000 : 0, // Default to 0 if dt is invalid
                    y: typeof yValue === 'number' ? yValue : 0, // Default to 0 if yValue is invalid
                  };
                })
                .filter((item) => 
                  (item.x > 0 && item.y >= 0)), // Filter valid entries
            },
          ];

          return { data: series };
        }
      },
    }),
  }),
});

export const { useGetSingleEventQuery, useGetChartQuery } = singleEventApi;
