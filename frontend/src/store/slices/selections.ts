import { createSlice } from '@reduxjs/toolkit';
import locations from '../../constants/locations';
import categories from '../../constants/categories';
import weathers from '../../constants/weathers';

interface SelectionsProps {
  selectedLocation: string | undefined;
  selectedCategory: string | undefined;
  selectedWeather: string | undefined;
  selectedDate: string | null;  
  locationList: string[];
  categoryList: string[];
  weatherList: string[];
}

const initialState: SelectionsProps = {
  selectedLocation: undefined,
  selectedCategory: undefined,
  selectedWeather: undefined,
  selectedDate: null,
  locationList: locations,
  categoryList: categories,
  weatherList: weathers,
};

export const Selections = createSlice({
  name: 'selections',
  initialState,
  reducers: {
    setSelectedLocation: (state, action) => {
      state.selectedLocation = action.payload;
    },
    setSelectedCategory: (state, action) => {
      state.selectedCategory = action.payload;
    },
    setSelectedWeather: (state, action) => {
      state.selectedWeather = action.payload;
    },
    setSelectedData: (state, action) => {
      state.selectedDate = action.payload;
    },
    setLocationList: (state, action) => {
      state.locationList = action.payload;
    },
    setCategoryList: (state, action) => {
      state.categoryList = action.payload;
    },
    setWeatherList: (state, action) => {
      state.weatherList = action.payload;
    },
  },
});

export const {
  setSelectedLocation,
  setSelectedCategory,
  setSelectedWeather,
  setSelectedData,
  setLocationList,
  setCategoryList,
  setWeatherList,
} = Selections.actions;
export default Selections.reducer;
