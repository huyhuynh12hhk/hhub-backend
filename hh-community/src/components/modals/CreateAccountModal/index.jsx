import {
  Box,
  Button,
  Card,
  CardContent,
  Divider,
  FormControl,
  FormHelperText,
  IconButton,
  Input,
  InputAdornment,
  InputLabel,
  OutlinedInput,
  TextField,
  Typography,
} from "@mui/material";
import ModalContainer from "../FormModalContainer";
import { useEffect, useState } from "react";
import {
  generateRandomPassword,
  generateUsernames as generateRandomUsernames,
} from "../../../utils/autoCredential";
import CopyClipboardButton from "../../CopyClipboardButton";
import { Visibility, VisibilityOff } from "@mui/icons-material";
import RefreshIcon from "@mui/icons-material/Refresh";
import { registerAccount } from "../../../services/accountService";

const CreateAccountModal = ({ open, setOpen }) => {
  const [username, setUsername] = useState(generateRandomUsernames());
  const [password, setPassword] = useState(generateRandomPassword());
  const [showPassword, setShowPassword] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();
    console.log("Start sign up....");
    try {
      const response = await registerAccount(username, password);
      console.log("Response body:", response.data);


      //   navigate("/");
    } catch (error) {
      const errorResponse = error.response.data;
      //   setSnackBarMessage(errorResponse.message);
      //   setSnackBarOpen(true);
    }
    setUsername(generateRandomUsernames())
    setPassword(generateRandomPassword())
    setOpen(false)
  };

  const renderBody = () => {
    return (
      <CardContent>
        <Typography variant="h5" component="h1" gutterBottom>
          Quick start with Guest account
        </Typography>
        <Typography
          variant="subtitle1"
          fontSize={11}
          sx={{ fontStyle: "italic" }}
          gutterBottom
        >
          In HH Blog ecosystem we give guest account an unique identifier as
          Username (UID). After create account, you can login with the UID key
          and your secret password.
        </Typography>
        <Box
          display="flex"
          flexDirection="column"
          alignItems="center"
          justifyContent="center"
          width="100%"
        >
          <FormControl fullWidth variant="standard">
            <InputLabel>UID</InputLabel>
            <Input
              name="username"
              type="text"
              sx={{
                backgroundColor: "#D3D3D3",
                padding: 1,
                marginBottom: 1,
              }}
              readOnly
              value={username}
              endAdornment={
                <InputAdornment position="end">
                  <IconButton
                    aria-label={"reset"}
                    onClick={() => {
                      setUsername(generateRandomUsernames());
                    }}
                    edge="end"
                  >
                    <RefreshIcon />
                  </IconButton>
                  <IconButton aria-label={"copy"} edge="end">
                    <CopyClipboardButton text={username} />
                  </IconButton>
                </InputAdornment>
              }
            />
          </FormControl>
          <FormControl fullWidth variant="standard">
            <InputLabel>Password</InputLabel>
            <Input
              name="password"
              type={showPassword ? "text" : "password"}
              sx={{
                padding: 1,
                marginBottom: 1,
              }}
              onChange={(e) => setPassword(e.target.value)}
              value={password}
              error={password.length > 20 || password.length < 4}
              placeholder="4-20 characters"
              endAdornment={
                <InputAdornment position="end">
                  <IconButton
                    aria-label={
                      showPassword
                        ? "hide the password"
                        : "display the password"
                    }
                    onClick={() => setShowPassword(!showPassword)}
                    edge="end"
                  >
                    {showPassword ? <VisibilityOff /> : <Visibility />}
                  </IconButton>
                  <IconButton aria-label={"copy"} edge="end">
                    <CopyClipboardButton text={password} />
                  </IconButton>
                </InputAdornment>
              }
            />
            {(password.length > 20 || password.length < 4) && (
              <FormHelperText sx={{
                color:"red"
              }}>
                Password must between 4-20 characters
              </FormHelperText>
            )}
          </FormControl>
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
            Start
          </Button>
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
      submitText={"Sign Up"}
      // title={"Register"}
    >
      {renderBody()}
    </ModalContainer>
  );
};

export default CreateAccountModal;
