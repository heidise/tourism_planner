import { Box, CircularProgress } from '@mui/material';
import React from 'react';
import ReactApexChart from 'react-apexcharts';

interface ChartProps {
  series: { name: string; data: { x: number; y: number }[] }[];
  isLoading: boolean;
  isError: boolean;
  unit: string;
}

const Chart: React.FC<ChartProps> = ({ isLoading, isError, series, unit }) => {
  const options = {
    chart: {
      height: 350,
      background: 'white',
      zoom: {
        enabled: true,
      },
      foreColor: 'black',
      dropShadow: {
        enabled: true,
        top: 0,
        left: 0,
        blur: 2,
        color: '#185452',
        opacity: 0.35
      },
    },
    xaxis: {
      type: 'datetime' as const,
    },
    yaxis: {
      title: {
        text: unit,
      },
      labels: {
        formatter: (value: number) => value.toFixed(2),
      },
    },
    tooltip: {
      theme: 'dark',
      onDatasetHover: {
        highlightDataSeries: false,
      },
      x: {
        show: false,
        format: "HH:00"
      },
    },
    dataLabels: {
      enabled: false,
    },
    colors:['#15CAD4', 'red', 'green'],
  };

  return (
    <Box
      display="flex"
      height={350}
      width="100%"
      flexGrow={1}
      alignItems="center"
      justifyContent="center"
    >
      {isLoading ? (
        <CircularProgress />
      ) : isError ? (
        <Box>Couldn't find weather data for given date</Box>
      ) : (
          <Box width="100%" height="100%">
            <ReactApexChart
              type={(unit === 'mm'?'bar':'line')} // Bar chart for precipitation, line for the rest.
              options={options}
              series={JSON.parse(JSON.stringify(series))} 
              height={350}
              width="100%"
            />
        </Box>
      )}
    </Box>
  );
};

export default Chart;
