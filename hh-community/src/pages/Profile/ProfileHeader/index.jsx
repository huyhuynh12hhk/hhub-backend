import { Avatar, Box, Typography } from "@mui/material";
import DefaultBackground from '../../../assets/default-background.png'

const ProfileHeader = ({cover, avatar, username, fullName}) => {

    return (
      <Box 
        sx={{ 
            position: 'relative', 
            mb: 8,
            width: '100%'
        }}
    >
        {/* Cover / Background Image */}
        <Box
          sx={{
            height: 200,
            width: '100%',
            backgroundImage: `url(${cover || DefaultBackground})`,
            backgroundSize: 'cover',
            backgroundPosition: 'center',
          }}
        />
  
        {/* Avatar (overlapping the cover image) */}
        <Avatar
          alt="User Name" 
          src={avatar}
          sx={{
            width: 100,
            height: 100,
            border: '4px solid white',
            position: 'absolute',
            top: 150, // Adjust so it overlaps the cover image
            left: 16,
          }}
        />
  
        {/* User Details */}
        <Box sx={{ ml: 16, mt: 8 }}>
          <Typography variant="h5">{fullName}</Typography>
          <Typography variant="body1" color="textSecondary">
            @{username}
          </Typography>
        </Box>
      </Box>
    );
  };
  
  export default ProfileHeader;