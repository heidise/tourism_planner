import { Typography, Stack, Link } from '@mui/material';
import Grid from '@mui/material/Grid2';
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import AccessTimeIcon from '@mui/icons-material/AccessTime';
import LocationOnIcon from '@mui/icons-material/LocationOn';
import LocationCityIcon from '@mui/icons-material/LocationCity';
import LaunchIcon from '@mui/icons-material/Launch';

interface EventDetailsProps {
    city: string;
    startTime: string;
    location: string;
    imageUrl: string;
    address: string;
    url: string;
    info: string;
    description: string;
}
  
const EventDetails = ({
    city,
    startTime,
    location,
    imageUrl,
    address,
    url,
    info,
    description,
}: EventDetailsProps) => {

    return (
        <Grid container spacing={2}>
            <Grid size={5} justifyContent={'center'} flexBasis={'content'} margin={'0px 20px 20px 0px'}>
                <img src={imageUrl} alt='Picture of the event' 
                style={{minWidth: '250px', maxWidth: '600px',
                boxShadow: '0 4px 8px 0 black, 0 6px 20px 0 black'}}>
                </img>
            </Grid>

            <Grid size={6}>
                <Stack spacing={1} margin={'0px 0px 20px 0px'}>
                <Stack direction={'row'} alignItems={'center'} gap={1}>
                    <CalendarMonthIcon />
                    <Typography>{new Date(startTime).toLocaleDateString()}</Typography>
                </Stack>

                <Stack direction={'row'} alignItems={'center'} gap={1}>
                    <AccessTimeIcon />
                    <Typography>{new Date(startTime).toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" })}</Typography>
                </Stack>

                <Stack direction={'row'} alignItems={'center'} gap={1}>
                    <LocationCityIcon />
                    <Typography>{location}</Typography>
                </Stack>

                <Stack direction={'row'} alignItems={'center'} gap={1}>
                    <LocationOnIcon />
                    <Typography>{address}, {city}</Typography>
                </Stack>

                <Stack direction={'row'} alignItems={'center'} gap={1}>
                    <LaunchIcon />
                    <Link variant='body1' href={url}>Purchase tickets here</Link>
                </Stack>

                <Stack direction={'row'} alignItems={'center'} gap={1}>
                    <Typography>{(description == '')?'No description available':description}</Typography>
                    <Typography>{'\n'} {(info == '')?'':'Extra information: '} {(info == '')?'':info}</Typography>
                </Stack>
                </Stack>
            </Grid>
            </Grid>
    );
};

export default EventDetails;