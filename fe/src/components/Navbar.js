// Navbar.js
import React from 'react';
import '../css/Navbar.css';

const Navbar = ({ setScreen }) => {
  return (
    <div className="navbar">
      <button onClick={() => setScreen('home')}>TastyHub</button>
      <button onClick={() => setScreen('recipe')}>레시피</button>
      <button onClick={() => setScreen('')}>재료 공유</button>
      <button onClick={() => setScreen('')}>채팅</button>
      <button onClick={() => setScreen('')}>마이페이지</button>
      {/* 기타 버튼들 */}
    </div>
  );
};

export default Navbar;
