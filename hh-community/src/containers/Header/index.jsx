import Grid from "@mui/material/Grid2";
import SearchBar from "../../components/SearchBar";
import { Typography } from "@mui/material";

const AppHeader = () => {
  return (
    <Grid id="app=header" container height={"100%"} width={'100%'} marginX={5} justifyContent="center" alignItems="center">
        <Typography
            fontWeight={800}
        >
            HH blog 
        </Typography>
      <SearchBar />
    </Grid>
  );
};

export default AppHeader;
