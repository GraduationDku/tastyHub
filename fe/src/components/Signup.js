import React, { useState } from 'react';

function Signup({ setScreen }) {
  const [username,setUsername] = useState('');
  const [password,setPassword] = useState('');
  const [nickname,setNickname] = useState('');
  const [email,setEmail] = useState('');
  const [usernameAvailable, setUsernameAvailable] = useState('');
  const [nicknameAvailable, setNicknameAvailable] = useState('');
  const [emailAvailable, setEmailAvailable] = useState('');

  const usernamePattern = /^[a-zA-Z0-9]+$/; // 영어와 숫자만
  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // 기본 이메일 형식
  const validateUsername = (username) => usernamePattern.test(username);
  const validateEmail = (email) => emailPattern.test(email);


  const handleSignup = async (e) => {
   e.preventDefault();

   const isUsernameValid = validateUsername(username);
   const isEmailValid = validateEmail(email);

   setUsernameAvailable(isUsernameValid);
   setEmailAvailable(isEmailValid);

   if (!isUsernameValid || !isEmailValid) {
    // 유효하지 않은 입력이 있으면 여기서 처리
    console.error('입력이 유효하지 않습니다.');
    alert('잘못된 입력입니다.');
    return;
  }

   const checkUsername = async (e) => {
    try {
      const response = await fetch('http://localhost:3000/user/overlap/username', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          nickname,
        }),
      });
      if (response.ok) {
        alert('사용 가능한 이름입니다.');
      } else {
        console.error('사용 불가능한 이름입니다.');
      }
    } catch (error) {
      console.error('닉네임 입력 중 오류 발생:', error);
    }
   }

   const checkNickname = async (e) => {
    try {
      const response = await fetch('http://localhost:3000/user/overlap/nickname', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          nickname,
        }),
      });
      if (response.ok) {
        alert('사용 가능한 이름입니다.');
      } else {
        console.error('사용 불가능한 이름입니다.');
      }
    } catch (error) {
      console.error('닉네임 입력 중 오류 발생:', error);
    }
   }
   const checkEmail = async (e) => {
    try {
      const response = await fetch('http://localhost:3000/email/verified', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          username,
        }),
      });
      if (response.ok) {
        alert('사용 가능한 이메일입니다.');
      } else {
        console.error('사용 불가능한 이메일입니다.');
      }
    } catch (error) {
      console.error('이메일 검증 중 오류 발생:', error);
    }
   }
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
      PW :
      <input
        type="text"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <br></br>
      <br></br>
      닉네임 :
      <input
        type="text"
        placeholder="이름을 입력하세요"
        value={nickname}
        onChange={(e) => setNickname(e.target.value)}
      />
      <br></br>
      <br></br>
      E-Mail :
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