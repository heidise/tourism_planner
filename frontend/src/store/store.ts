import { configureStore } from '@reduxjs/toolkit';
import { setupListeners } from '@reduxjs/toolkit/query';
import selections from './slices/selections';
import { eventsApi } from './services/events';
import { singleEventApi } from './services/singleEvent';

export const store = configureStore({
  reducer: {
    selections,
    [eventsApi.reducerPath]: eventsApi.reducer,
    [singleEventApi.reducerPath]: singleEventApi.reducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware().concat(eventsApi.middleware, singleEventApi.middleware),
});

setupListeners(store.dispatch);

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
