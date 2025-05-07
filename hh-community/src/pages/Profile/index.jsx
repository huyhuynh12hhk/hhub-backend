import { useEffect, useState } from "react";
import useIsAuthenticated from "../../utils/hooks/useIsAuthenticated";
import { useNavigate, useParams } from "react-router-dom";
import { Box, Card, Divider, Typography } from "@mui/material";
import { SafetyDividerOutlined } from "@mui/icons-material";
import ProfileHeader from "./ProfileHeader";
import { fetchProfile } from "../../services/profileService";

const Profile = () => {
  const navigate = useNavigate();
  const { uid } = useParams();
  const [profileDetails, setProfileDetails] = useState({});
  const isAuthenticated = useIsAuthenticated();

  const getProfileDetails = async () => {
    try {
      console.log(uid);
      const response = await fetchProfile(uid);
      console.log("Profile: ",response.data)
      setProfileDetails(response.data);
    } catch (error) {
      console.log("Error "+error)
      navigate("/")
    }
  };

  // useEffect(() => {
  //   getProfileDetails()
  //   setProfileDetails(profile_data);
  // }, []);

  useEffect(() => {
    if (!isAuthenticated) {
      navigate("/");
    } else {
      getProfileDetails();
    }
  }, [navigate]);

  const renderProfile = () => {
    return (
      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          alignItems: "flex-start",
          width: "100%",
          gap: "10px",
        }}
      >
        <ProfileHeader
          avatar={profileDetails.profilePicture}
          cover={profileDetails.profileCover}
          fullName={profileDetails.fullName}
          username={profileDetails.username}
        />

        <Divider />
        <Box
          sx={{
            display: "flex",
            flexDirection: "row",
            justifyContent: "flex-start",
            alignItems: "flex-start",
            width: "100%", // Ensure content takes full width
            gap:1
          }}
        >
          <Typography
            sx={{
              fontSize: 14,
              fontWeight: 600,
            }}
          >
            Join at {" "}
          </Typography>
          <Typography
            sx={{
              fontSize: 14,
              fontWeight: 300,
              backgroundColor:'lightgray'
            }}
          >
            {new Date(profileDetails.dateJoined).toLocaleDateString()}
          </Typography>
          
        </Box>
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            justifyContent: "flex-start",
            alignItems: "flex-start",
            width: "100%", // Ensure content takes full width
          }}
        >
          <Typography
            sx={{
              fontSize: 14,
              fontWeight: 600,
            }}
          >
            Full Name
          </Typography>
          <Typography
            sx={{
              fontSize: 14,
            }}
          >
            {profileDetails.fullName}
          </Typography>
        </Box>
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            justifyContent: "flex-start",
            alignItems: "flex-start",
            width: "100%", // Ensure content takes full width
          }}
        >
          <Typography
            sx={{
              fontSize: 14,
              fontWeight: 600,
            }}
          >
            Bio
          </Typography>
          <div>
            <Typography
              sx={{
                fontSize: 14,
              }}
            >
              {profileDetails.bio || "Not set"}
            </Typography>
          </div>
        </Box>
        <Box
          sx={{
            display: "flex",
            flexDirection: "row",
            justifyContent: "space-between",
            alignItems: "flex-start",
            width: "100%", // Ensure content takes full width
          }}
        ></Box>
      </Box>
    );
  };

  return (
    <Card
      sx={{
        width: "80vw",
        minWidth: 350,
        maxWidth: 800,
        boxShadow: 3,
        borderRadius: 2,
        padding: 4,
        marginLeft: 10,
      }}
    >
      <h1>User {profileDetails.fullName} profile</h1>
      {renderProfile()}
    </Card>
  );
};

export default Profile;
