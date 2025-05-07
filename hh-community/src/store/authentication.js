import {createSlice} from '@reduxjs/toolkit';
import { AUTHENTICATION } from './constants';

const initialState = {
  accessToken: null,
  refreshToken: null,
};

const authentication = createSlice({
  initialState,
  name: AUTHENTICATION,
  reducers: {
    resetAuthentication: () => initialState,
    setAuthentication: (state, {payload}) => {
      return payload;
    },
  },
});

export const {resetAuthentication, setAuthentication} = authentication.actions;
export default authentication.reducer;
