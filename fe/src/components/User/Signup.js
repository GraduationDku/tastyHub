import React, { useState } from 'react';
import '../../css/User/Signup.css';

function Signup({ setScreen }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [nickname, setNickname] = useState('');
  const [email, setEmail] = useState('');
  const [verifiedCode, setVerifiedCode] = useState('');
  const [usernameAvailable, setUsernameAvailable] = useState(false);
  const [nicknameAvailable, setNicknameAvailable] = useState(false);
  const [emailAvailable, setEmailAvailable] = useState(false);
  const [verificationSuccess, setVerificationSuccess] = useState(false);
  const [passwordsMatch, setPasswordsMatch] = useState(true);
  const [usernameButtonText, setUsernameButtonText] = useState("아이디 확인");
  const [nicknameButtonText, setNicknameButtonText] = useState("닉네임 확인");
  const [emailButtonText, setEmailButtonText] = useState("이메일 중복 확인");
  const [verifyButtonText, setVerifyButtonText] = useState("인증번호 확인");

  
  const handlePasswordMatchCheck = () => {
    if (password === confirmPassword) {
      setPasswordsMatch(true);
      alert('비밀번호가 일치합니다.');
    } else {
      setPasswordsMatch(false);
      alert('비밀번호가 일치하지 않습니다.');
    }
  };

  const checkUsernameAvailability = async () => {
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/user/overlap/username?username=${username}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      if (response.ok) {
        alert('사용 가능한 아이디입니다.');
        setUsernameAvailable(true);
        setUsernameButtonText("확인되었습니다.");
      } else {
        const errorText = await response.text();
        alert(`오류가 발생했습니다: ${errorText}`);
        setUsernameAvailable(false);
      }
    } catch (error) {
      console.error('아이디 검사 중 오류 발생:', error);
    }
  };

  const checkNicknameAvailability = async () => {
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/user/overlap/nickname?nickname=${nickname}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      if (response.ok) {
        alert('사용 가능한 닉네임입니다.');
        setNicknameAvailable(true);
        setNicknameButtonText("확인되었습니다.");
      } else {
        const errorText = await response.text();
        alert(`오류가 발생했습니다: ${errorText}`);
        setNicknameAvailable(false);
      }
    } catch (error) {
      console.error('닉네임 검사 중 오류 발생:', error);
    }
  };

  const sendVerificationCode = async () => {
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/email`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ "email": email }),
      });
      if (response.ok) {
        alert('인증번호가 이메일로 발송되었습니다.');
      } else {
        const errorText = await response.text();
        alert(`오류가 발생했습니다: ${errorText}`);
      }
    } catch (error) {
      console.error('이메일 인증번호 발송 중 오류 발생:', error);
    }
  };

  const verifyCode = async () => {
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/email/verified`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ "email": email, "verifiedCode": verifiedCode }),
      });
      if (response.ok) {
        const verificationResult = await response.json();
        if (verificationResult) { // 인증번호가 일치할 경우
          alert('이메일 인증 성공!');
          setVerificationSuccess(true);
          setVerifyButtonText("확인되었습니다.");
          setEmailAvailable(true); // 인증 성공 시, emailAvailable도 true로 설정
        } else {
          alert('인증번호가 일치하지 않습니다.');
          setVerificationSuccess(false);
          setEmailAvailable(false); // 인증 실패 시, emailAvailable도 false로 설정
        }
      } else {
        const errorText = await response.text();
        alert(`오류가 발생했습니다: ${errorText}`);
        setVerificationSuccess(false);
        setEmailAvailable(false); // 서버 에러 발생 시, emailAvailable도 false로 설정
      }
    } catch (error) {
      console.error('인증번호 검증 중 오류 발생:', error);
      setVerificationSuccess(false);
      setEmailAvailable(false); // 예외 발생 시, emailAvailable도 false로 설정
    }
  };
  
  

  const handleSignup = async (e) => {
    e.preventDefault();
    if (!usernameAvailable || !nicknameAvailable || !emailAvailable || !passwordsMatch) {
      alert('모든 필드의 유효성을 확인하세요.');
      return;
    }

    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/user/signup`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          "username": username,
          "password": password,
          "nickname": nickname,
          "email": email,
        }),
      });
      if (response.ok) {
        const authorization = response.headers.get('Authorization');
        const refreshToken = response.headers.get('Refresh');
        localStorage.setItem('accessToken', authorization);
        localStorage.setItem('refreshToken', refreshToken);
        console.log('회원가입 성공');
        setScreen('village');
      } else {
        console.error('회원가입 실패');

      }
    } catch (error) {
      console.error('회원가입 중 오류 발생:', error);
    }
  };

  return (
    <div className='signup'>
      <div className='box'>
        <h2>회원가입</h2>
        <div className='inbox'>
          <span className="label1">ID :</span>
          <input
            type="text"
            placeholder="ID"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <button onClick={checkUsernameAvailability}>{usernameButtonText}</button>
          <br /><br />
          <span className="label2">PW :</span>
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <br /><br />
          <span className="label6"></span>
          <input
            type="password"
            placeholder="Confirm Password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
          />
          <button onClick={handlePasswordMatchCheck}>비밀번호 일치 확인</button>
          <br /><br />
          <span className="label3">닉네임 :</span>
          <input 
            type="text"
            placeholder="닉네임을 입력하세요"
            value={nickname}
            onChange={(e) => setNickname(e.target.value)}
          />
          <button onClick={checkNicknameAvailability}>{nicknameButtonText}</button>
          <br /><br />
          <span className="label4">E-Mail :</span>
          <input
            type="text"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <button onClick={sendVerificationCode}>이메일 인증번호 보내기</button>
          <br /><br />
          <span className="label5">PIN :</span>
          <input
            type="text"
            placeholder="Enter verification code"
            value={verifiedCode}
            onChange={(e) => setVerifiedCode(e.target.value)}
          />
          <button onClick={verifyCode}>{verifyButtonText}</button>
          <br /><br />
          <button onClick={() => setScreen('village')}>동네 정보 입력</button>
          <button onClick={handleSignup} >회원가입하기</button>
          
        </div>
      </div>
    </div>
  );
}

export default Signup;
