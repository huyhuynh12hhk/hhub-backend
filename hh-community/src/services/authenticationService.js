import { store } from "../store";
import { setAuthentication } from "../store/authentication";
import httpClient from "../utils/httpClient";

const baseUrl = `${process.env.REACT_APP_BASE_URL}/identity/auth`;

export const authenticate = async (username, password) => {
  const response = await httpClient.post(`${baseUrl}/token`, {
    username: username,
    password: password,
  });

  // console.log(response.data.data ?? "nothing");

  // setToken(response.data?.result?.token);

  store.dispatch(setAuthentication({
    accessToken: response.data.data.token
  }))

  return response.data;
};
