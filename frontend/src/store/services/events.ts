import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';

interface Event {
  id: string;
  name: string;
  location: string;
  date: string;
  category: string;
  weather: string;
}

interface GetEventsParams {
  location: string;
  category: string;
  weather: string;
  startDateTime: string;
  endDateTime: string;
}

export const eventsApi = createApi({
  reducerPath: 'eventsApi',
  baseQuery: fetchBaseQuery({ baseUrl: '/api' }), 
  endpoints: (builder) => ({
    getEvents: builder.query<Event[], GetEventsParams>({
      query: ({ location, category, weather, startDateTime, endDateTime }) => ({
        url: '/events',
        params: {
          location,
          category,
          weather,
          startDateTime,
          endDateTime
        }
      }),
    }),
  }),
});

export const { useGetEventsQuery } = eventsApi;
