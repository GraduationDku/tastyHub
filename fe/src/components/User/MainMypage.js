import React, { useEffect } from 'react';

function MainMypage({ setScreen, isGuest }) {
  const nickname = localStorage.getItem('nickname');

  useEffect(() => {
    console.log('MainMypage useEffect called', { isGuest }); // 디버깅용 콘솔 로그 추가
    if (isGuest) {
      setScreen('signup');
      return;
    }
  }, [isGuest, setScreen]);

  return (
    <div className='mainmypage'>
      <div className='box'>
        <br/>
        
        <h1>{nickname}의 마이페이지</h1>
        <br /><br />
        <button onClick={() => setScreen('mypageedit')}>내 정보 수정하기</button>
        <br />
        <button onClick={() => setScreen('mypageshow')}> 조회하기 </button>
      </div>
    </div>
  );
}

export default MainMypage;
