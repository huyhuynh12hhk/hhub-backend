import { useSelector } from "react-redux";
import { getSelf } from "../selectors/state";
import { authorizationHeaders } from "../utils/authentication";
import httpClient from "../utils/httpClient";

const baseUrl = `${process.env.REACT_APP_BASE_URL}/blog/comments`;

export const getComments = async (postId) => {
  const response = await httpClient.get(
    `${baseUrl}?postId=${postId}`,
    authorizationHeaders()
  );


  return response.data;
};



export const createComment = async (postId, content, userInfo) => {
  const response = await httpClient.post(
    `${baseUrl}`,
    {
      postId: postId,
      content: content,
      authorId: userInfo.id,
      authorName: userInfo.username,
    },
    authorizationHeaders()
  );

  return response.data;
};

export const updateComment = async (content) => {
  const response = await httpClient.put(
    `${baseUrl}`,
    {
      content: content,
    },
    authorizationHeaders()
  );

//   console.log(response.data.data);

  return response.data;
};

export const deleteComment = async (id) => {
  const response = await httpClient.delete(
    `${baseUrl}/${id}`,
    {},
    authorizationHeaders()
  );

  console.log(response.data.data);

  return response.data;
};
