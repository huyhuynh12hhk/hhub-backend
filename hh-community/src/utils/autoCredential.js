import { v4 as uuidv4 } from "uuid";

export const generateRandomPassword = (length = 10) => {
  const chars =
    "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()";
  let result = "";
  for (let i = 0; i < length; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length));
  }
  return result;
};

export const generateUsernames = () => {
  const luid = uuidv4().split("-");
  const uid = luid.join('').slice(0, 18);
  return `UID${uid}`;
};
