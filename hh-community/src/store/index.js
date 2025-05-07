
import {combineReducers, configureStore} from '@reduxjs/toolkit';
import {FLUSH, PAUSE, PERSIST, persistReducer, persistStore, PURGE, REGISTER, REHYDRATE} from 'redux-persist';
import storage from 'redux-persist/lib/storage';

import authenticationReducer from './authentication';
import selfReducer from './self';

const persistConfig = {
    key: 'root',
    storage,
  };

const persistedReducer = persistReducer(
    persistConfig,
    combineReducers({
      authentication: authenticationReducer,
      self: selfReducer,
    }),
  );

export const store = configureStore({
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: [FLUSH, PAUSE, PERSIST, PURGE, REGISTER, REHYDRATE],
      },
    }),
  reducer: persistedReducer,
});


export const persistor = persistStore(store);
