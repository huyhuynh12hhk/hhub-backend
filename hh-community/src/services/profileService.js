import { authorizationHeaders } from "../utils/authentication";
import httpClient from "../utils/httpClient";

const baseUrl = `${process.env.REACT_APP_BASE_URL}/account/profiles`;

export const fetchProfile = async (uid) => {
  const response = await httpClient.get(`${baseUrl}/${uid}`, authorizationHeaders());

  return response.data;
};
