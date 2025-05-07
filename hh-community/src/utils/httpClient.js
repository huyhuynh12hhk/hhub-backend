import axios from "axios";
import { authorizationHeaders } from "./authentication";
import { store } from "../store";
import { notifyAuthorizeRequire, notifyErrorMessage } from "./toastHelper";
import { useAuthorizeRequire } from "../context/authorizeRequireContext";
import { logout } from "../services/accountService";

const BASE_URL = `${process.env.API_URL}/api/`;

const {
  authentication: { accessToken },
} = store.getState();

const httpClient = axios.create({
  baseURL: BASE_URL,
  timeout: 30000,
  headers: {
    Authorization:
      accessToken && accessToken.length > 0 ? authorizationHeaders() : null,

    "Content-Type": "application/json",
  },
});

httpClient.interceptors.response.use(
  (response) => {
    return response;
  },
  async function (error) {
    const originalRequest = error.config;
    console.log(error.response.data);
    if (error.response.status === 401) {
      console.log("Token has been expired");
      await logout();
    }
    if (
      error.response.status === 404 &&
      error.response.data.message === "User not existed"
    ) {
      notifyErrorMessage("Invalid username or password!")
    }
    if (error.response.status === 400) {
      console.log("Something went wrong. Error at: " + error);
      notifyErrorMessage("Ops!! Something went wrong, try again!");
    }
  }
);

export default httpClient;
