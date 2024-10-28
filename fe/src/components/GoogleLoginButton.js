import React from "react";
import { GoogleLogin } from '@react-oauth/google';
import { useDispatch } from 'react-redux';
import { loginUser } from '../../src/redux/User/loginState.js'; // Adjust the path

function GoogleLoginButton() {
  const dispatch = useDispatch();

  const handleLoginSuccess = async (credentialResponse) => {
    console.log("Google Credential Response:", credentialResponse); // 확인 1: Google에서 반환된 credentialResponse 확인
    const tokenId = credentialResponse.credential;
    console.log("Token ID:", tokenId);

    // Optionally, send tokenId to the server for validation/authentication
    const response = await fetch(`https://localhost:443/oauth2/login/google`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ tokenId }),
    });
    console.log("Server Response:", response); // 확인 3: 서버 응답 확인

    const data = await response.json();
    console.log("Response Data:", data); // 확인 4: 서버에서 반환된 데이터 확인
    const authorization = response.headers.get('Authorization');
    console.log("Authorization Header:", authorization); // 확인 5: Authorization 헤더 확인


    if (authorization) {
      console.log("Dispatching loginUser with:", { authorization, nickname: data.nickname }); // 확인 6: Redux 액션 디스패치 시 값 확인
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
