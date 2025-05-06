import { Route, Routes } from "react-router-dom";
import Feed from "../../pages/Feed";
import Profile from "../../pages/Profile";
import * as S from './Styles';
import Callback from "../../pages/Callback";
import Login from "../../pages/SSOLogin";

const Main = ({ className }) => {
  return (
    <S.Container className={className} id="scrollableDiv" style={{ height: '100vh', overflow: "auto" }}>
      
      <Routes>
        <Route path="/feed" element={<Feed />} />

        <Route path="/" element={<Feed />} />
        <Route path="/sso-login" element={<Login />} />
        <Route path="/profile/:uid" element={<Profile />} />
        <Route path="/auth/callback" element={<Callback />} />
      </Routes>
    </S.Container>
  );
};

export default Main;
