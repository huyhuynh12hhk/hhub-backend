import { useSelector } from "react-redux";
import { getSelf } from "../../selectors/state";


const useIsAuthenticated = () => {
  const self = useSelector(getSelf);

  return !!self.username;
};

export default useIsAuthenticated;
