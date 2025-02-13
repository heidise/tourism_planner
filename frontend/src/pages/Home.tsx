import dayjs from 'dayjs';
import {
  Box,
  Button,
  CircularProgress,
  Divider,
  Typography,
} from '@mui/material';
import { StaticDatePicker } from '@mui/x-date-pickers';
import { useDispatch, useSelector } from 'react-redux';
import EventListItem from '../components/EventListItem';
import {
  setSelectedCategory,
  setSelectedData,
  setSelectedLocation,
  setSelectedWeather,
} from '../store/slices/selections';
import { RootState } from '../store/store';
import Select from '../components/Select';
import { useGetEventsQuery } from '../store/services/events';
import { useEffect } from 'react';

const Home = () => {
  const dispatch = useDispatch();

  const selectedLocation = useSelector(
    (state: RootState) => state.selections.selectedLocation
  );
  const locationList = useSelector(
    (state: RootState) => state.selections.locationList
  );
  const selectedCategory = useSelector(
    (state: RootState) => state.selections.selectedCategory
  );
  const categoryList = useSelector(
    (state: RootState) => state.selections.categoryList
  );
  const selectedWeather = useSelector(
    (state: RootState) => state.selections.selectedWeather
  );
  const weatherList = useSelector(
    (state: RootState) => state.selections.weatherList
  );
  const selectedDate = useSelector(
    (state: RootState) => state.selections.selectedDate
  );

  const { data, isLoading, isFetching, isError } = useGetEventsQuery(
    {
      location: selectedLocation || '',
      category: selectedCategory === 'All' ? '' : selectedCategory || '',
      weather: selectedWeather ? selectedWeather : '',
      startDateTime: selectedDate
        ? dayjs(selectedDate).subtract(1, 'day').unix().toString()
        : '',
      endDateTime: selectedDate
        ? dayjs(selectedDate).add(1, 'day').unix().toString()
        : '',
    },
    {
      skip: !selectedLocation || !selectedCategory || !selectedDate,
    }
  );

  const savePreferenceHandler = () => {
    localStorage.setItem('selectedCategory', JSON.stringify(selectedCategory));
    localStorage.setItem('selectedLocation', JSON.stringify(selectedLocation));
    localStorage.setItem('selectedWeather', JSON.stringify(selectedWeather));
  };

  useEffect(() => {
    const storedCategory = JSON.parse(
      localStorage.getItem('selectedCategory') || '""'
    );
    const storedLocation = JSON.parse(
      localStorage.getItem('selectedLocation') || '""'
    );
    const storedWeather = JSON.parse(
      localStorage.getItem('selectedWeather') || '""'
    );

    if (storedCategory) {
      dispatch(setSelectedCategory(storedCategory));
    }

    if (storedLocation) {
      dispatch(setSelectedLocation(storedLocation));
    }

    if (storedWeather) {
      dispatch(setSelectedWeather(storedWeather));
    }
  }, [dispatch]);

  return (
    <Box display={'flex'} flexDirection={'column'} width={'100vw'}>
      {/* Header */}
      <Box
        display={'flex'}
        height={'68px'}
        bgcolor={'#15CAD4'}
        justifyContent={'center'}
      >
        <Typography variant="h3" color="white">
          Tourism Planner
        </Typography>
      </Box>

      {/* Divider */}
      <Box
        display={'flex'}
        height={'10px'}
        bgcolor={'white'}
        flexGrow={1}
        border={'1px solid'}
        borderColor={'black'}
      ></Box>

      {/* Body */}
      <Box
        display={'flex'}
        flexDirection={'column'}
        gap={'30px'}
        height={'calc(100vh - 142px)'}
        bgcolor={'#DCDCDC'}
        flexGrow={1}
        padding={'30px'}
      >
        {/* Overlay with CircularProgress */}
        {(isLoading || isFetching) && (
          <Box
            position="absolute"
            top={0}
            left={0}
            width="100%"
            height="100%"
            display="flex"
            justifyContent="center"
            alignItems="center"
            zIndex={10}
          >
            <CircularProgress />
          </Box>
        )}

        {/* Selectors */}
        <Box
          display={'flex'}
          flexDirection={'row'}
          justifyContent={'space-between'}
        >
          <Box display={'flex'} flexDirection={'row'} gap={'80px'}>
            <Select
              id="location-selector"
              value={selectedLocation}
              options={locationList}
              label="Location"
              onChange={(value) => dispatch(setSelectedLocation(value))}
            />
            <Select
              id="category-selector"
              value={selectedCategory}
              options={categoryList}
              label="Category"
              onChange={(value) => dispatch(setSelectedCategory(value))}
            />
            <Select
              id="weather-selector"
              value={selectedWeather}
              options={weatherList}
              label="Weather"
              onChange={(value) => {
                dispatch(setSelectedWeather(value));
              }}
            />
          </Box>
          <Button
            variant="contained"
            size="small"
            sx={{
              height: '30px',
              marginTop: '30px',
              bgcolor: '#2C2C2C',
              textTransform: 'none',
            }}
            // add weather as well when available
            disabled={
              selectedCategory === undefined || selectedLocation === undefined
            }
            onClick={savePreferenceHandler}
          >
            Save as preference
          </Button>
        </Box>

        {/* Calendar & Events */}
        <Box
          display={'flex'}
          flexDirection={'column'}
          gap={'20px'}
          padding={'20px'}
          bgcolor={'white'}
          maxHeight={'calc(100vh - 260px)'}
          overflow={'auto'}
        >
          <Box
            display={'flex'}
            flexDirection={'column'}
            gap={'10px'}
            alignItems={'center'}
          >
            <StaticDatePicker
              orientation="landscape"
              value={selectedDate ? dayjs(selectedDate) : null}
              onChange={(value) => {
                dispatch(setSelectedData(value ? value.toISOString() : null));
              }}
              /* min date today */
              minDate={dayjs()}
              sx={{
                bgcolor: '#ECE6F0',
                borderRadius: '25px',
                height: '300px',
              }}
            />
          </Box>

          {/* Divider */}
          <>
            <Divider />
            {/* Error messages */}
            <Box display="flex" flexDirection="column" gap="10px">
              {!selectedLocation && (
                <Typography variant="body1" color="error">
                  Location is not selected, please select the date to see events
                </Typography>
              )}
              {!selectedCategory && (
                <Typography variant="body1" color="error">
                  Category is not selected, please select the date to see events
                </Typography>
              )}
              {!selectedWeather && (
                <Typography variant="body1" color="error">
                  Weather is not selected, please select the date to see events
                </Typography>
              )}
              {!selectedDate && (
                <Typography variant="body1" color="error">
                  Date is not selected, please select the date to see events
                </Typography>
              )}

              {isError && (
                <Typography variant="body1" color="error">
                  Error fetching events!
                </Typography>
              )}

              {/* Events list */}
              {selectedLocation &&
                selectedCategory &&
                selectedDate &&
                Array.isArray(data) &&
                data.length > 0 &&
                data.map((event, index) => (
                  <EventListItem
                    key={index}
                    id={event.id}
                    name={event.name}
                    // @ts-expect-error - ts- error
                    startTime={event.startTime}
                    location={event.location}
                    // @ts-expect-error - ts- error
                    imageUrl={event.imageUrl}
                  />
                ))}

              {selectedLocation &&
                selectedCategory &&
                selectedDate &&
                Array.isArray(data) &&
                data.length === 0 && (
                  <Typography variant="body1" color="error">
                    No events found!
                  </Typography>
                )}
            </Box>
          </>
        </Box>
      </Box>
    </Box>
  );
};

export default Home;
