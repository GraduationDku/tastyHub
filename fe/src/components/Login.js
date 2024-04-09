import React, { useState } from 'react';

function Login({ setScreen }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch('/user/login', {
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
      alert("아이디 및 비밀번호가 일치하지 않습니다.");
    }
  };
  return (
    <div>
      <h2>Login</h2>
      <input
        type="text"
        placeholder="ID"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      />
      <input
        type="password"
        placeholder="PASSWORD"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <button onClick={handleLogin}>로그인</button>
      <button onClick={() => setScreen('signup')}>Sign Up</button>
    </div>
  );
  };
  


export default Login;
