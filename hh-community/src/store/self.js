import {createSlice, PayloadAction} from '@reduxjs/toolkit';
import { SELF } from './constants';


const initialState = {
  accountNumber: '',
  balance: 0,
  displayImage: '',
  displayName: '',
  signingKey: '',
};

const self = createSlice({
  initialState,
  name: SELF,
  reducers: {
    resetSelf: () => initialState,
    setSelf: (state, {payload}) => {
      return payload;
    },
    updateSelf: (state, {payload}) => {
      Object.assign(state, payload);
    },
  },
});

export const {resetSelf, setSelf, updateSelf} = self.actions;
export default self.reducer;
