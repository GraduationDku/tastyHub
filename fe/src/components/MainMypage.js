import React from 'react';

function MainMypage({ setScreen }) {
  const nickname = localStorage.getItem('nickname');

  return (
    <div className='mainmypage'>
      <h1>{nickname}님 환영합니다</h1>
      <button onClick={() => setScreen('mypageedit')}>Edit</button>
      <button>View</button>
    </div>
  );
}

export default MainMypage;
