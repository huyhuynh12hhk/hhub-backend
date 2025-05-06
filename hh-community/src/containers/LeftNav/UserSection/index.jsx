import { Avatar } from "@mui/material";
import Grid from "@mui/material/Grid2";
import { Link } from "react-router-dom";

const UserSection = ({uid}) => {
    

  return (
    <Link to={`profile/${uid}`}>
      <Grid
        width="100%"
        display="flex"
        justifyContent="center"
        alignItems="center"
      >
        <Avatar sx={{ width: 80, height: 80 }} />
      </Grid>
    </Link>
  );
};

export default UserSection;
