import {
  Box,
  Button,
  CardContent,
  Divider,
  TextField,
  Typography,
} from "@mui/material";
import { useEffect, useState } from "react";
import ModalContainer from "../FormModalContainer";
import { authenticate } from "../../../services/authenticationService";
import { getInfo } from "../../../services/accountService";
import toast from "react-hot-toast";
import { notifySuccessMessage } from "../../../utils/toastHelper";

const LoginModal = ({ open, setOpen }) => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = async (event) => {
    event.preventDefault();
    console.log("Start logging in....");
    try {
      const response = await authenticate(username, password);
      const info = await getInfo();

      if(info.code === 200){
        notifySuccessMessage("Login success!")
      }
    } catch (error) {
      const errorResponse = error.response;
      console.log("Error: "+errorResponse)
      return
    }
    setOpen(false);
  };

  const renderBody = () => {
    return (
      <CardContent>
        <Typography variant="h5" component="h1" gutterBottom>
          Welcome to HH wallpaper
        </Typography>
        <Box
          display="flex"
          flexDirection="column"
          alignItems="center"
          justifyContent="center"
          width="100%"
        >
          <TextField
            label="Username"
            variant="outlined"
            fullWidth
            margin="normal"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <TextField
            label="Password"
            type="password"
            variant="outlined"
            fullWidth
            margin="normal"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <Button
            type="submit"
            variant="contained"
            color="primary"
            size="large"
            onClick={handleSubmit}
            fullWidth
            sx={{
              mt: "15px",
              mb: "25px",
            }}
          >
            Login
          </Button>
          {/* <Divider>Or</Divider>
          <Button
            type="submit"
            variant="contained"
            color="success"
            size="large"
          >
            Create new account here
          </Button> */}
        </Box>
      </CardContent>
    );
  };

  return (
    <ModalContainer
      open={open}
      setOpen={setOpen}
      onSubmit={(formData) => {
        console.log(formData);
        alert("submit form");
      }}
      submitText={"Login"}
      // title={"Login"}
    >
      {renderBody()}
    </ModalContainer>
  );
};

export default LoginModal;
