import httpClient from "../utils/httpClient";
import { store } from "../store";
import {
  resetAuthentication,
  setAuthentication,
} from "../store/authentication";
import { resetSelf, setSelf } from "../store/self";
import { useSelector } from "react-redux";
import { getAuthToken } from "../selectors/state";
import { authorizationHeaders } from "../utils/authentication";

const baseUrl = `${process.env.REACT_APP_BASE_URL}/identity/users`;

export const getInfo = async () => {
  const response = await httpClient.get(
    `${baseUrl}/info`,
    authorizationHeaders()
  );

  // console.log(response.data.data);

  store.dispatch(setSelf(response.data.data));

  return response.data;
};

export const logout = () => {
  store.dispatch(resetAuthentication());
  store.dispatch(resetSelf());
  window.location.href = "/";
};

export const registerAccount = async (username, password) => {
  const response = await httpClient.post(
    `${baseUrl}/registration`,
    {
        "username":username,
        "password":password
    }
  );

  return response.data
};
