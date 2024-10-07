import React, { useEffect } from 'react';
import '../../css/MainMypage.css';

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
    <body>
      
        <br/>
        
        <h1>{nickname}님, 환영합니다 !</h1>
        <br/><br/>
       <div className='mainbox'> 
        
        <button onClick={() => setScreen('mypageedit')}>내 정보 수정하기</button>
        <br />
        <button onClick={() => setScreen('mypageshow')}> 조회하기 </button>
      </div>
    </body>
  );
}

export default MainMypage;
