import React, { useState } from 'react';
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
  const [currentStep, setCurrentStep] = useState(1);

  const {
    userName, password, confirmPassword, nickname, email, verifiedCode,
    usernameAvailable, nicknameAvailable, emailAvailable, passwordsMatch,
    usernameButtonText, nicknameButtonText, emailButtonText, verifyButtonText
  } = useSelector((state) => state.signup);

  const handleChange = (field, value) => {
    dispatch(updateField({ field, value }));
  };

  const handleNextStep = () => {
    setCurrentStep((prev) => prev + 1);
  };

  const handlePrevStep = () => {
    setCurrentStep((prev) => (prev > 1 ? prev - 1 : prev));
  };

  const handleSignup = async (e) => {
    e.preventDefault();
    if (!usernameAvailable || !nicknameAvailable || !emailAvailable || !passwordsMatch) {
      alert('모든 필드의 유효성을 확인하세요.');
      return;
    }
    dispatch(signupUser({ userName, password, nickname, email }))
      .then(() => setCurrentStep(currentStep + 1)); // 동네 입력 버튼 단계로 이동
  };

  return (
    <body className='signup'>
      <div className='signupbox'>
        <div className='inbox'>
          {currentStep === 1 && (
            <>
              <h3 className="label1">아이디를 입력해주세요</h3>
              <input
                type="text"
                placeholder="아이디를 입력하세요"
                value={userName}
                onChange={(e) => handleChange('userName', e.target.value)}
              />
              <button onClick={() => dispatch(checkUsernameAvailability(userName))}>
                {usernameButtonText}
              </button>
            </>
          )}

          {currentStep === 1 && (
            <>
              <h3 className="label2">비밀번호를 입력해주세요</h3>
              <input
                type="password"
                placeholder="비밀번호를 입력하세요"
                value={password}
                onChange={(e) => handleChange('password', e.target.value)}
              />
              <br />
              <span className="label6">비밀번호 확인</span>
              <input
                type="password"
                placeholder="비밀번호를 다시 입력하세요"
                value={confirmPassword}
                onChange={(e) => handleChange('confirmPassword', e.target.value)}
              />
              <button onClick={() => dispatch(checkPasswordsMatch())}>비밀번호 일치 확인</button>
              <div style={{ display: 'flex', flexDirection: 'row',
                alignItems: 'center', justifyContent: 'space-between'}}>
                <button onClick={handlePrevStep}>이전</button>
                <button onClick={handleNextStep}>다음</button>
              </div>
            </>
          )}

          {currentStep === 2 && (
            <>
              <span className="label3">닉네임</span>
              <input
                type="text"
                placeholder="닉네임을 입력하세요"
                value={nickname}
                onChange={(e) => handleChange('nickname', e.target.value)}
              />
              <button onClick={() => dispatch(checkNicknameAvailability(nickname))}>
                {nicknameButtonText}
              </button>
              <div style={{ display: 'flex', flexDirection: 'row',
                alignItems: 'center', justifyContent: 'space-between'}}>
                <button onClick={handlePrevStep}>이전</button>
                <button onClick={handleNextStep}>다음</button>
              </div>
            </>
          )}

          {currentStep === 3 && (
            <>
              <span className="label4">이메일</span>
              <input
                type="text"
                placeholder="이메일을 입력하세요"
                value={email}
                onChange={(e) => handleChange('email', e.target.value)}
              />
              <button onClick={() => dispatch(sendVerificationCode(email))}>
                {emailButtonText}
              </button>
              <br />
              <span className="label5">인증번호</span>
              <input
                type="text"
                placeholder="인증번호를 입력하세요"
                value={verifiedCode}
                onChange={(e) => handleChange('verifiedCode', e.target.value)}
              />
              <button onClick={() => dispatch(verifyCode({ email, verifiedCode }))}>
                {verifyButtonText}
              </button>
              <div style={{ display: 'flex', flexDirection: 'row',
                alignItems: 'center', justifyContent: 'space-between'}}>
                <button onClick={handlePrevStep}>이전</button>
                <button onClick={handleNextStep}>다음</button>
              </div>
            </>
          )}

          {currentStep === 4 && (
            <>
              <h3 className='label1'>회원가입 정보를 확인하고 완료하세요!</h3>
              <ul>
                <li>아이디: {userName}</li>
                <li>닉네임: {nickname}</li>
                <li>이메일: {email}</li>
              </ul>
              <div style={{ display: 'flex', flexDirection: 'row',
                alignItems: 'center', justifyContent: 'space-between'}}>
              <button onClick={handlePrevStep}>이전</button>
              <button onClick={handleSignup}>위치 정보 설정하기</button></div>
            </>
          )}

          {currentStep === 5 && (
            <>
              <h3 className='label1'>회원가입이 완료되었습니다!</h3>
              <p>동네 정보를 입력해주세요.</p>
              <button onClick={() => setScreen('village')}>동네 정보 입력</button>
            </>
          )}
        </div>
      </div>
    </body>
  );
}

export default Signup;
