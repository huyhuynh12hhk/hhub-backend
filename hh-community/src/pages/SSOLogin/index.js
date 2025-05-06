
export default function Login() {
  const authorize = () => {
    window.location.href =
      "http://localhost:9000/oauth2/authorize?response_type=code" +
      "&client_id=nsa2&scope=openid profile" +
      "&redirect_uri=http://localhost:3000/auth/callback";
  };

  return <button onClick={authorize}>Login with SSO</button>;
}
