import React from 'react';
import '../css/HomeScreen.css';
import icon from '../assets/icon.png'; // 이미지 경로 설정

function HomeScreen({ setScreen, setIsGuest, handleGuestAccess }) {
  return (
    <div className='parent'>
      <img src={icon} alt='Icon' className='icon' />
      <h1 className='title'>TastyHub</h1>
      <button className="top" onClick={handleGuestAccess}>비회원</button>
      <br /><br />
      <button className="bottom" onClick={() => {
        setIsGuest(false);
        setScreen('login');
      }}>회원</button>
    </div>
  );
}

export default HomeScreen;
