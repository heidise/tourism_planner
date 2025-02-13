import React from 'react';
import {
  Select as MuiSelect,
  MenuItem,
  FormControl,
  Typography,
  Box,
} from '@mui/material';

interface SelectProps {
  id: string;
  value?: string;
  options: string[];
  onChange: (value: string) => void;
  label?: string;
}

const Select: React.FC<SelectProps> = ({
  id,
  value,
  options,
  onChange,
  label,
}) => {
  const transformedOptions = options.map((option) => ({
    label: option,
    value: option,
  }));

  return (
    <Box id={id} display={'flex'} flexDirection={'column'}>
      <Typography
        variant="caption"
        fontWeight={'bold'}
        sx={{
          paddingLeft: '5px',
        }}
      >
        {label}
      </Typography>
      <FormControl sx={{ m: 1, minWidth: 200 }} size="small">
        <MuiSelect
          value={value || ''}
          onChange={(e) => onChange(e.target.value)}
          sx={{
            bgcolor: 'white',
          }}
        >
          {transformedOptions.map((option) => (
            <MenuItem key={option.value} value={option.value}>
              {option.label}
            </MenuItem>
          ))}
        </MuiSelect>
      </FormControl>
    </Box>
  );
};

export default Select;
