import { useSelector } from "react-redux";
import { getSelf } from "../selectors/state";
import { authorizationHeaders } from "../utils/authentication";
import httpClient from "../utils/httpClient";

const baseUrl = `${process.env.REACT_APP_BASE_URL}/blog/posts`;

export const fetchPosts = async (page = 1, size = 5) => {
  const response = await httpClient.get(
    `${baseUrl}?page=${page}&size=${size}`
    // , authorizationHeaders()
  );

  // console.log(response.data.data);

  return response.data;
};

export const fetchPostDetail = async (id) => {
  const response = await httpClient.get(`${baseUrl}/${id}`);

  // console.log(response.data.data);

  return response.data;
};

export const createPost = async (content, userInfo) => {
  const response = await httpClient.post(
    `${baseUrl}`,
    {
      content: content,
      authorId: userInfo.id,
      authorName: userInfo.username,
    },
    authorizationHeaders()
  );

  return response.data;
};

export const updatePost = async (id, content) => {
  const response = await httpClient.put(
    `${baseUrl}`,
    {
      content: content,
    },
    authorizationHeaders()
  );

  console.log(response.data.data);

  return response.data;
};

export const reactPost = async (id, userInfo) => {
  const response = await httpClient.post(
    `${baseUrl}/${id}/reactions`,
    {
      id: userInfo.id,
      name: userInfo.username,
    },
    authorizationHeaders()
  );

  return response.data;
};

export const deletePost = async (id) => {
  const response = await httpClient.delete(
    `${baseUrl}/${id}`,
    {},
    authorizationHeaders()
  );

  console.log(response.data.data);

  return response.data;
};
