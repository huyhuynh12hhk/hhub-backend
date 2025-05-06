import { useEffect } from "react";

const LoginPage = () => {
  const url = "http://nsa2-gateway:8080/user/login";

  useEffect(() => {
    console.log("onLoad");
    window.location.href = url;
  }, [url]);

  return (
    <div>
      <h1>Redirecting to OAuth2 Server</h1>
    </div>
  );
};

export default LoginPage;
