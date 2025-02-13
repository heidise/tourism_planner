import { Box, Typography, Avatar } from '@mui/material';
import { useNavigate } from 'react-router';

interface EventListItemProps {
  id: string;
  name: string;
  startTime: string;
  location: string;
  imageUrl: string;
}

const EventListItem = ({
  id,
  name,
  startTime,
  location,
  imageUrl,
}: EventListItemProps) => {
  const navigate = useNavigate();

  return (
    <Box
      display={'flex'}
      flexDirection={'row'}
      gap={'10px'}
      padding={'15px'}
      alignItems={'center'}
      border={'1px solid'}
      borderColor={'#D9D9D9'}
      borderRadius={'5px'}
      boxShadow={'0px 4px 6px rgba(0, 0, 0, 0.1)'}
      sx={{
        cursor: 'pointer',
        transition: 'box-shadow 0.3s ease',
        ':hover': {
          boxShadow: '0px 6px 12px rgba(0, 0, 0, 0.15)',
          backgroundColor: '#f5f5f5',
        },
      }}
      onClick={() => navigate('/event', {state: {id: id, name: name}})}
    >
      <Avatar
        variant="square"
        src={imageUrl}
        alt={name}
        sx={{ width: 50, height: 50 }}
      />
      <Box display="flex" flexDirection="column" justifyContent="center">
        <Typography variant="body1" fontWeight="bold">
          {name}
        </Typography>
        <Typography variant="body2" color="textSecondary">
          {new Date(startTime).toLocaleString()}
        </Typography>
        <Typography variant="body2" color="textSecondary">
          {location}
        </Typography>
      </Box>
    </Box>
  );
};

export default EventListItem;
