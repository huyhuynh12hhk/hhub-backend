import { Route, Routes } from "react-router-dom";
import Feed from "../../pages/Feed";
import Profile from "../../pages/Profile";
import * as S from './Styles';

const Main = ({ className }) => {
  return (
    <S.Container className={className}>
      <Routes>
        <Route path="/feed" element={<Feed />} />

        <Route path="/" element={<Feed />} />
        <Route path="/profile/:uid" element={<Profile />} />
      </Routes>
    </S.Container>
  );
};

export default Main;
