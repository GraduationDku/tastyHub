import React, { useState } from 'react';
import FindUsername from "../components/FindUsername";

function Login({ setScreen }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [showFindUsername, setShowFindUsername] = useState(false);

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch('http://localhost:8080/user/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          username,
          password,
        }),
      });

      if (response.ok) {
        const authorization = response.headers.get('Authorization');
        const refreshToken = response.headers.get('Refresh');
        localStorage.setItem('accessToken', authorization);
        localStorage.setItem('refreshToken', refreshToken);
        console.log('로그인 성공');
      } else {
        console.error('로그인 실패');
        alert("아이디 및 비밀번호가 일치하지 않습니다.");
      }
    } catch (error) {
      console.error('로그인 중 오류 발생:', error);
    }
  };
  return (
    <div>
      <h2>로그인</h2>
      <input
        type="text"
        placeholder="ID"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      />
      <br></br>
      <br></br>
      <input
        type="password"
        placeholder="PASSWORD"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <br></br>
      <br></br>
      <button onClick={handleLogin}>로그인</button>
      <button onClick={() => setScreen('signup')}>Sign Up</button>
      <button onClick={() => setShowFindUsername(true)}>아이디 찾기</button>
      {showFindUsername && <FindUsername setVisible={setShowFindUsername} />}
    </div>
  );
  };
  


export default Login;