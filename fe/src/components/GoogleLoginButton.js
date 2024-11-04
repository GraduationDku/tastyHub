import React from "react";
import { GoogleLogin } from '@react-oauth/google';
import { useDispatch } from 'react-redux';
import { loginUser } from '../../src/redux/User/loginState.js'; // Adjust the path

function GoogleLoginButton() {
  const dispatch = useDispatch();

  const handleLoginSuccess = async (credentialResponse) => {
    const tokenId = credentialResponse.credential;
    console.log(tokenId)
    const response = await fetch(`https://localhost/oauth2/authorization/google?tokenId=${tokenId}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
    });

    const data = await response.json();
    const authorization = response.headers.get('Authorization');

    if (authorization) {
      // Dispatch to Redux to handle successful login
      dispatch(loginUser({ authorization, nickname: data.nickname }));
    } else {
      console.log("Google login failed");
    }
  };

  return (
    <GoogleLogin
      onSuccess={handleLoginSuccess}
      onError={() => console.log("Login Failed")}
    />
  );
}

export default GoogleLoginButton;
