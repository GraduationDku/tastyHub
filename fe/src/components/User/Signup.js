// src/components/User/Signup.js
import React from 'react';
import { useSelector, useDispatch } from 'react-redux';
import '../../css/Signup.css';

import {
  updateField,
  checkPasswordsMatch,
  checkUsernameAvailability,
  checkNicknameAvailability,
  sendVerificationCode,
  verifyCode,
  signupUser,
} from '../../redux/User/signupState';

function Signup({ setScreen }) {
  const dispatch = useDispatch();
  const {
    userName, password, confirmPassword, nickname, email, verifiedCode,
    usernameAvailable, nicknameAvailable, emailAvailable, passwordsMatch,
    usernameButtonText, nicknameButtonText, emailButtonText, verifyButtonText
  } = useSelector((state) => state.signup);

  const handleChange = (field, value) => {
    dispatch(updateField({ field, value }));
  };

  const handleSignup = async (e) => {
    e.preventDefault();
    if (!usernameAvailable || !nicknameAvailable || !emailAvailable || !passwordsMatch) {
      alert('모든 필드의 유효성을 확인하세요.');
      return;
    }
    dispatch(signupUser({ userName, password, nickname, email }))
      .then(() => setScreen('village'));
  };

  return (
    <body className='signup'>
      <div className='signupbox'>
        <h2>회원가입</h2>
        <div className='inbox'>
          <span className="label1"></span>
          <input
            type="text"
            placeholder="아이디를 입력하세요"
            value={userName}
            onChange={(e) => handleChange('userName', e.target.value)}
          />
          <button onClick={() => dispatch(checkUsernameAvailability(userName))}>
            {usernameButtonText}
          </button>
          <br /><br />
          <span className="label2"></span>
          <input
            type="password"
            placeholder="비밀번호를 입력하세요"
            value={password}
            onChange={(e) => handleChange('password', e.target.value)}
          />
          <br /><br />
          <span className="label6"></span>
          <input
            type="password"
            placeholder="비밀번호를 다시 입력하세요"
            value={confirmPassword}
            onChange={(e) => handleChange('confirmPassword', e.target.value)}
          />
          <button onClick={() => dispatch(checkPasswordsMatch())}>비밀번호 일치 확인</button>
          <br /><br />
          <span className="label3"></span>
          <input 
            type="text"
            placeholder="닉네임을 입력하세요"
            value={nickname}
            onChange={(e) => handleChange('nickname', e.target.value)}
          />
          <button onClick={() => dispatch(checkNicknameAvailability(nickname))}>
            {nicknameButtonText}
          </button>
          <br /><br />
          <span className="label4"></span>
          <input
            type="text"
            placeholder="이메일을 입력하세요"
            value={email}
            onChange={(e) => handleChange('email', e.target.value)}
          />
          <button onClick={() => dispatch(sendVerificationCode(email))}>
            {emailButtonText}
          </button>
          <br /><br />
          <span className="label5"></span>
          <input
            type="text"
            placeholder="인증번호를 입력하세요"
            value={verifiedCode}
            onChange={(e) => handleChange('verifiedCode', e.target.value)}
          />
          <button onClick={() => dispatch(verifyCode({ email, verifiedCode }))}>
            {verifyButtonText}
          </button>
          <br /><br />
          <button onClick={() => setScreen('village')}>동네 정보 입력</button>
          <button onClick={handleSignup}>회원가입하기</button>
        </div>
      </div>
    </body>
  );
}

export default Signup;
