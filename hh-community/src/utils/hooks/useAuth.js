import { createContext, useContext } from "react";
import { useLocalStorage } from "./useLocalStorage";



const initialAuthContext = {
    authenticated: false,
    setAuthenticated: () => {},
    username: '',
    setUsername: () => {}
}

const AuthContext = createContext(initialAuthContext);

const AuthProvider = ({ children }) => {

    const [authenticated, setAuthenticated] = useLocalStorage('authenticated', false)
    const [username, setUsername] = useLocalStorage('username', '')

    const value = {
        authenticated,
        setAuthenticated,
        username,
        setUsername
    };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}

const useAuth = () => {
  return useContext(AuthContext);
}

export { AuthContext, AuthProvider, useAuth };  