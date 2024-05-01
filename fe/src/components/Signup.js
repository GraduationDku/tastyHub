import React, { useState } from 'react';

function Signup({ setScreen }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [nickname, setNickname] = useState('');
  const [email, setEmail] = useState('');
  const [verifiedCode, setVerifiedCode] = useState(false);
  const [usernameAvailable, setUsernameAvailable] = useState(false);
  const [nicknameAvailable, setNicknameAvailable] = useState(false);
  const [emailAvailable, setEmailAvailable] = useState(false);
  const [verificationSuccess, setVerificationSuccess] = useState(false);

  const usernamePattern = /^[a-zA-Z0-9]+$/; // 영어와 숫자만
  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // 기본 이메일 형식
  const validateUsername = (username) => usernamePattern.test(username);
  const validateEmail = (email) => emailPattern.test(email);

  const checkUsernameAvailability = async () => {
    try {
      const response = await fetch('http://localhost:8080/user/overlap/username', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username }),
      });
      const result = await response.json();
      if (result.available) {
        alert('사용 가능한 아이디입니다.');
        setUsernameAvailable(true);
      } else {
        alert('사용 불가능한 아이디입니다.');
        setUsernameAvailable(false);
      }
    } catch (error) {
      console.error('아이디 검사 중 오류 발생:', error);
    }
  };

  const checkNicknameAvailability = async () => {
    try {
      const response = await fetch('http://localhost:8080/user/overlap/nickname', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ nickname }),
      });
      const result = await response.json();
      if (result.available) {
        alert('사용 가능한 닉네임입니다.');
        setNicknameAvailable(true);
      } else {
        alert('사용 불가능한 닉네임입니다.');
        setNicknameAvailable(false);
      }
    } catch (error) {
      console.error('닉네임 검사 중 오류 발생:', error);
    }
  };

  const checkEmailAvailability = async () => {
    if (!validateEmail(email)) {
      alert('잘못된 이메일 형식입니다.');
      setEmailAvailable(false);
      return;
    }
    try {
      const response = await fetch('http://localhost:8080/email', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email }),
      });
      if (response.ok) {
        alert('인증번호가 이메일로 발송되었습니다.');
      } else {
        alert('중복된 이메일입니다.');
      }
    } catch (error) {
      console.error('인증번호 요청 중 오류 발생:', error);
    }
  };

  const verifyCode = async () => {
    try {
      const response = await fetch('http://localhost:8080/email/verified', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, verifiedCode }),
      });
      const result = await response.json();
      if (result.verified) {
        alert('이메일 인증 성공!');
        setVerificationSuccess(true);
      } else {
        alert('인증번호가 일치하지 않습니다.');
        setVerificationSuccess(false);
      }
    } catch (error) {
      console.error('인증번호 검증 중 오류 발생:', error);
      setVerificationSuccess(false);
    }
  };

  const handleSignup = async (e) => {
    e.preventDefault();
    if (!usernameAvailable || !nicknameAvailable || !emailAvailable) {
      alert('모든 필드의 유효성을 확인하세요.');
      return;
    }

    try {
      const response = await fetch('http://localhost:8080/user/signup', {
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
        localStorage.setItem('refreshToken', refreshToken);
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
      ID:
      <input
        type="text"
        placeholder="ID"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      />
      <button onClick={checkUsernameAvailability}>아이디 확인</button>
      <br /><br />
      PW:
      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <br /><br />
      닉네임:
      <input
        type="text"
        placeholder="닉네임을 입력하세요"
        value={nickname}
        onChange={(e) => setNickname(e.target.value)}
      />
      <button onClick={checkNicknameAvailability}>닉네임 확인</button>
      <br /><br />
      E-Mail:
      <input
        type="text"
        placeholder="Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      <button onClick={checkEmailAvailability}>이메일 인증번호 보내기</button>
      <br /><br />
      인증번호:
      <input
        type="text"
        placeholder="Enter verification code"
        value={verifiedCode}
        onChange={(e) => setVerifiedCode(e.target.value)}
      />
      <button onClick={verifyCode}>인증번호 확인</button>
      <br /><br />
      <button onClick={handleSignup} disabled={!usernameAvailable || !nicknameAvailable || !emailAvailable}>회원가입하기</button>
    </div>
  );
}

export default Signup;
