import { Box, Toolbar } from "@mui/material";
import LeftNav from "../LeftNav";
import * as S from "./Styles";
import Main from "../Main";
import { Outlet } from "react-router-dom";
import AppHeader from "../Header";
import { Toaster } from "react-hot-toast";
import { RequireAuthorizeNotificationProvider } from "../../context/authorizeRequireContext";

const drawerWidth = 300;

const MainLayout = ({ className }) => {
  return (
    <RequireAuthorizeNotificationProvider>
      <S.Header>
        <AppHeader />
      </S.Header>
      <S.Container className={className}>
        <LeftNav />
        <Main />
      </S.Container>
      <Toaster
        toastOptions={{
          success: {
            style:{
              backgroundColor: "#008000",
              color: "white"
            }
          },
          error:{
            style:{
              backgroundColor: "#ff4545",
              color: "white"
            }
          }
        }}
      />
    </RequireAuthorizeNotificationProvider>
  );
};

export default MainLayout;
