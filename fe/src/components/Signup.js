import React, { useState } from 'react';

function Signup({ setScreen }) {
  const [username,setUsername] = useState('')
  const [password,setPassword] = useState('')
  const [nickname,setNickname] = useState('')
  const [email,setEmail] = useState('')

  const handleSignup = async (e) => {
   e.preventDefault();
   try {
    const response = await fetch('/user/signup', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        username,
        password,
        nickname,
        email,
      }),
    });
    if (response.ok) {
      const authorization = response.headers.get('Authorization');
      const refreshToken = response.headers.get('Refresh');
      localStorage.setItem('accessToken', authorization);
      localStorage.setItem('refreshToken',refreshToken);
      console.log('회원가입 성공');
      setScreen('login');
    } else {
      console.error('회원가입 실패');
    }
  } catch (error) {
    console.error('회원가입 중 오류 발생:', error);
  }
  };

  return (
    <div>
      <h2>회원가입</h2>
      ID : 
      <input
        type="text"
        placeholder="ID"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      />
      <br></br>
      <br></br>
      PW
      <input
        type="text"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <br></br>
      <br></br>
      닉네임
      <input
        type="text"
        placeholder="이름을 입력하세요"
        value={nickname}
        onChange={(e) => setNickname(e.target.value)}
      />
      <br></br>
      <br></br>
      E-Mail
      <input
        type="text"
        placeholder="Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      <br></br>
      <br></br>
      <button onClick={handleSignup}>회원가입하기</button>
    </div>
  );
}


export default Signup;