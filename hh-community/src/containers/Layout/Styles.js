import styled from "styled-components";
import { breakpoints } from '../../styles'

export const Container = styled.div`
  display: flex;
  justify-content: start;
  height: 100vh;
  margin: 0;
  overflow-y: hidden;

  @media (max-width: ${breakpoints.desktop}) {
    grid-template-columns: 150px 1fr;
  }
`;

export const Header = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 60px;
  flex-wrap: wrap;
  background-color: #ffffff;
  border-bottom: 1px solid #a59898;
  display: flex;
  align-items: center;
  z-index: 1000;
  justify-content:start;
  padding:0 20px;

  
`;
