import styled from "styled-components";
import { colors, hiddenScroll } from "../../styles";

export const Container = styled.div`
  margin-top: 60px;
  padding: 0 10vw;
  width: 100%;
  min-width:500px;
  border-right: 1px solid ${colors.border};
  ${hiddenScroll};
`;
