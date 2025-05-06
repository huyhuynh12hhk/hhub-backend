import { Button, Divider, List, Toolbar } from "@mui/material";
// import LogoutIcon from "@mui/icons-material/Logout";
import UserSection from "./UserSection";
import useIsAuthenticated from "../../utils/hooks/useIsAuthenticated";
import UnauthenticatedSection from "./UnauthenticatedSection";

import HomeIcon from "@mui/icons-material/Home";
import AccountBoxIcon from "@mui/icons-material/AccountBox";
import DevicesOtherIcon from "@mui/icons-material/DevicesOther";

import * as S from "./Styles";
import NavItem from "./NavItem";
import { useSelector } from "react-redux";
import { getSelf } from "../../selectors/state";
import { logout } from "../../services/accountService";

const LeftNav = ({ className }) => {
  const isAuthenticated = useIsAuthenticated();

  const self = useSelector(getSelf);

  const renderUserSection = () => {
    if (!isAuthenticated) return null;
    return <UserSection uid={self.id}/>;
  };

  const renderUnauthenticatedSection = () => {
    if (isAuthenticated) return null;
    return <UnauthenticatedSection />;
  };

  const renderMenuItemsSection = () => {
    return (
      <List>
        <NavItem icon={<HomeIcon />} text={"Feed"} to="/" />
        {
          isAuthenticated?
          <NavItem
            icon={<AccountBoxIcon />}
            text={"Profile"}
            to={`/profile/${self.id}`}
          />
          :null
        }
        <NavItem icon={<DevicesOtherIcon />} text={"Others"} to="#" disabled/>
      </List>
    );
  };

  const renderLogoutButton = () => {
    if (!isAuthenticated) return null;
    return (
      <Button
        fullWidth
        variant="contained"
        color="primary"
        // endIcon={<LogoutIcon />}
        onClick={() => {
          logout()
        }}
      >
        Logout
      </Button>
    );
  };

  return (
    <S.Container className={className}>
      <S.Top>

        {renderUserSection()}
        {renderUnauthenticatedSection()}
        <Divider />
        {renderMenuItemsSection()}
      </S.Top>
      {renderLogoutButton()}
    </S.Container>
  );
};

export default LeftNav;
