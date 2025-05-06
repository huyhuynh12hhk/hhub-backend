import { createContext, useContext, useState } from "react";
import LoginModal from "../components/modals/LoginModal";
import { notifyAuthorizeRequire } from "../utils/toastHelper";

const RequireAuthorizeNotification = createContext(null);

export const RequireAuthorizeNotificationProvider = ({ children }) => {
  const [loginOpen, setLoginOpen] = useState(false);

  const require = (notify = false) => {
    setLoginOpen(true);
    if (notify) notifyAuthorizeRequire();
  };

  return (
    <RequireAuthorizeNotification.Provider value={{ loginOpen, require, setLoginOpen }}>
      {children}
      <LoginModal open={loginOpen} setOpen={setLoginOpen} />
    </RequireAuthorizeNotification.Provider>
  );
};

export const useAuthorizeRequire = () => {
  const context = useContext(RequireAuthorizeNotification);
  if (context === null) {
    return null;
  }
  return context;
};
