import React from 'react';
import '../css/HomeScreen.css';

function HomeScreen({ setScreen, setIsGuest, handleGuestAccess }) {
  return (
    <div className="homescreen">
      <h1>TastyHub</h1>
      <button className='top' onClick={handleGuestAccess}>비회원</button>
      <br /><br />
      <button className='bottom' onClick={() => {
        setIsGuest(false);
        setScreen('login');
      }}>회원</button>
    </div>
  );
}

export default HomeScreen;
