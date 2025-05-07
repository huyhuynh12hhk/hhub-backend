import {
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
} from "@mui/material";
import { Link } from "react-router-dom";

const NavItem = ({ icon, text, to, disabled = false }) => {
  return (
    <ListItem key={"home"} disablePadding>
      <ListItemButton component={Link} to={to} disabled={disabled}>
        <ListItemIcon>{icon}</ListItemIcon>
        <ListItemText primary={text} />
      </ListItemButton>
    </ListItem>
  );
};

export default NavItem;
