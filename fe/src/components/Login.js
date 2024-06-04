import React, { useState } from 'react';
import '../css/Login.css';

function Login({ setScreen }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

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
        const data = await response.json(); // Extract JSON data
        const nickname = data.nickname; // Extract nickname from JSON data

        localStorage.setItem('accessToken', authorization);
        localStorage.setItem('refreshToken', refreshToken);
        localStorage.setItem('nickname', nickname);

        console.log(nickname);
        console.log(response);
        console.log(refreshToken);
        console.log(authorization);
        console.log('로그인 성공');
        setScreen('main');
      } else {
        console.error('로그인 실패');
        alert("아이디 및 비밀번호가 일치하지 않습니다.");
      }
    } catch (error) {
      console.error('로그인 중 오류 발생:', error);
    }
  };

  const handleFindUsername = () => {
    window.open('/findUsername', '_blank');
  };

  return (
    <div className='login'>
      <div className='box'>
        <h2>LOGIN</h2>
        <br/>
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
        <br/>
        <div className='button-container'>
          <button onClick={handleLogin}>로그인</button><br/>
          <br/><br/>
          <button onClick={() => setScreen('signup')}>회원가입</button>
          <button onClick={() => setScreen('findUsername')}>아이디 찾기</button>
        </div>
      </div>
    </div>
  );
}

export default Login;
