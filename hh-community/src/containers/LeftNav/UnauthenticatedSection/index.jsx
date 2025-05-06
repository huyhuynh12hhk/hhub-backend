import { Box, Button, Stack } from "@mui/material";
import Grid from "@mui/material/Grid2";
import { useState } from "react";
import LoginModal from "../../../components/modals/LoginModal";
import CreateAccountModal from "../../../components/modals/CreateAccountModal";
import { useAuthorizeRequire } from "../../../context/authorizeRequireContext";

const UnauthenticatedSection = () => {
  
  const loginRequire = useAuthorizeRequire()
  const [signUpOpen, setSignUpOpen] = useState(false);
  
  return (
    <>
      <Grid
        container
        spacing={0}
        direction="column"
        alignItems="center"
        justifyContent="center"
      >
        <Stack spacing={2} direction="row">
          <Button
            variant="contained"
            onClick={() => {
                loginRequire?.require()
            }}
            size="medium"
          >
            Login
          </Button>

          <Button
            variant="outlined"
            onClick={() => {
              setSignUpOpen(true)
            }}
            size="medium"
          >
            Sign Up
          </Button>
        </Stack>
      </Grid>
      <CreateAccountModal open={signUpOpen} setOpen={setSignUpOpen}/>

    </>
  );
};

export default UnauthenticatedSection;
