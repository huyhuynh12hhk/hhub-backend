// src/Callback.jsx
import React, { useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import httpClient from "../../utils/httpClient";

export default function Callback() {
  const navigate = useNavigate();
  const { search } = useLocation();

  useEffect(() => {
    async function fetchToken() {
      const params = new URLSearchParams(search);
      const code = params.get("code");

      const response = await httpClient.post(
        "http://localhost:9000/oauth2/token",
        {
          grant_type: "authorization_code",
          code,
          redirect_uri: "http://localhost:3000/auth/callback",
          client_id: "nsa2",
          code_verifier: sessionStorage.getItem("pkce_verifier"),
        }
      );
      sessionStorage.setItem("access_token", response.data.access_token);
      navigate.replace("/");
    }
    fetchToken();
  }, [search, navigate]);

  return <div>Loading...</div>;
}
