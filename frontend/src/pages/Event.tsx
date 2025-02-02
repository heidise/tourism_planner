import {
  Box,
  Button,
  Typography,
  Divider,
  ToggleButton,
  ToggleButtonGroup,
  CircularProgress,
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import React from 'react';
import { useNavigate, useLocation } from 'react-router';
import Chart from '../components/Chart';
import EventDetails from '../components/EventDetails';
import {
  useGetChartQuery,
  useGetSingleEventQuery,
} from '../store/services/singleEvent';

const Event = () => {
  const navigate = useNavigate();
  const { state } = useLocation();
  const { id, name } = state;

  const toggleButtonStyle = {
    color: 'white',
    backgroundColor: '#2C2C2C',
    textTransform: 'none',
    '&.Mui-selected': { bgcolor: 'grey', color: 'white' },
    '&.Mui-selected:hover': { bgcolor: 'grey' },
    ':hover': { bgcolor: '#15CAD4' },
  };

  const { data, isLoading, isFetching, isError } = useGetSingleEventQuery({
    id: id,
  });

  const [selectedGraph, setGraphSelection] = React.useState<string | null>(
    'temp'
  );

  const {
    data: chartData,
    isLoading: isChartLoading,
    isFetching: isChartFetching,
    isError: isChartError,
  } = useGetChartQuery({
    id: id,
    field: selectedGraph ? selectedGraph : 'temp',
  });

  const handleGraphSelection = (
    event: React.MouseEvent<HTMLElement>,
    newGraph: string | null
  ) => {
    if (!newGraph || newGraph === selectedGraph) {
      event.preventDefault(); // Prevent action if the same graph is selected
      return;
    }
    setGraphSelection(newGraph);
  };

  const getUnitForGraph = (selectedGraph: string | null): string => {
    const unitMap: Record<string, string> = {
      temp: '°C', // Temperature
      feelsLike: '°C', // Feels Like
      speed: 'm/s', // Wind Speed
      rain: 'mm', // Precipitation
    };

    return selectedGraph && unitMap[selectedGraph]
      ? unitMap[selectedGraph]
      : '';
  };

  return (
    <Box display={'flex'} flexDirection={'column'} width={'calc(100vw - 20px)'}>
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
        height={'100%'}
        width={'calc(100vw - 20px)'}
        flexGrow={1}
        bgcolor={'white'}
        justifyContent={'center'}
      >
        {/* Back button and event title */}
        <Box
          display={'flex'}
          flexDirection={'row'}
          gap={'100px'}
          margin={'20px 20px 20px 20px'}
          justifyContent={'center'}
        >
          <Button
            size={'medium'}
            variant={'outlined'}
            startIcon={<ArrowBackIcon />}
            style={{
              color: 'white',
              backgroundColor: '#2C2C2C',
              position: 'absolute',
              left: '20px',
              textTransform: 'none',
            }}
            onClick={() => navigate('/home')}
          >
            Back to search
          </Button>

          <Typography
            variant={'h4'}
            color={'black'}
            margin={'0px 0px 0px 55px'}
            style={{ textShadow: '1px 1px 2px #15CAD4' }}
          >
            {name}
          </Typography>
        </Box>

        {/*Loading event content*/}
        {isLoading && isFetching && (
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

        {/*If there is error fetching the event details*/}
        {isError && (
          <Typography variant="body1" color="error">
            Error fetching the event details.
          </Typography>
        )}

        {/* Event content */}
        {!isLoading && !isFetching && !isError && data && (
          <Box
            display={'flex'}
            flexDirection={'column'}
            justifyContent={'center'}
            bgcolor={'#DCDCDC'}
            padding={'20px'}
            margin={'0px 20px 0px 20px'}
            width={'calc(100vw - 105px)'}
          >
            <EventDetails
              city={data.simpleEvent.city}
              startTime={data.simpleEvent.startTime}
              location={data.simpleEvent.location}
              imageUrl={data.simpleEvent.imageUrl}
              address={data.address}
              url={data.url}
              info={data.info}
              description={data.description}
            />

            <Divider variant={'middle'} />

            {/* toggle buttons */}
            <ToggleButtonGroup
              value={selectedGraph}
              exclusive
              onChange={handleGraphSelection}
              aria-label="Weather graph selection"
              sx={{
                bgcolor: 'none',
                gap: '20px',
                margin: '20px 20px 20px 20px',
                justifyContent: 'center',
              }}
            >
              <ToggleButton
                value="temp"
                aria-label="Temperature graph"
                sx={toggleButtonStyle}
              >
                Temperature
              </ToggleButton>
              <ToggleButton
                value="feelsLike"
                aria-label="Feels like graph"
                sx={toggleButtonStyle}
              >
                Feels like
              </ToggleButton>
              <ToggleButton
                value="speed"
                aria-label="Windspeed graph"
                sx={toggleButtonStyle}
              >
                Windspeed
              </ToggleButton>
              <ToggleButton
                value="rain"
                aria-label="Precipitation graph"
                sx={toggleButtonStyle}
              >
                Precipitation
              </ToggleButton>
            </ToggleButtonGroup>

            {/* Chart */}
            <Box margin={'20px 0px 20px 0px'}>
              {chartData && (
                <Chart
                  key={selectedGraph}
                  series={chartData}
                  unit={getUnitForGraph(selectedGraph)}
                  isLoading={isChartLoading || isChartFetching}
                  isError={
                    isChartError ||
                    chartData.length === 0 ||
                    chartData[0]?.data.length === 0
                  }
                ></Chart>
              )}
            </Box>
          </Box>
        )}
      </Box>
    </Box>
  );
};

export default Event;
