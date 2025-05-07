import styled from "styled-components";

import { colors } from "./../../styles";


export const Container = styled.div`
  border-right: 1px solid ${colors.border};
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 24px 16px;
  margin-top:10vh;
`;

export const Top = styled.div`
  display: flex;
  width:250px;
  flex-direction: column;
  gap: 8px;
`;
