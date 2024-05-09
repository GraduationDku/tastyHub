import React, { useState } from 'react';
import '../css/Navbar.css';

const Navbar = ({ setScreen, onSearch }) => {
  const [searchTerm, setSearchTerm] = useState('');

  const handleSearchChange = (event) => {
    setSearchTerm(event.target.value);
  };

  const handleSearchSubmit = (event) => {
    event.preventDefault();
    if (onSearch) {
      onSearch(searchTerm); // 상위 컴포넌트로 검색어 전달
    }
    setSearchTerm(''); // 검색어 초기화
    setScreen('search'); // 검색 화면으로 전환
  };

  return (
    <div className="navbar">
      <button className='main' onClick={() => setScreen('main')}>TastyHub</button>
      <button onClick={() => setScreen('recipe')}>레시피</button>
      <button onClick={() => setScreen('')}>재료 공유</button>
      <button onClick={() => setScreen('')}>채팅</button>
      <button onClick={() => setScreen('')}>마이페이지</button>

      <form className="search-form" onSubmit={handleSearchSubmit}>
        <input
          type="text"
          placeholder="           레시피를 검색하세요 !"
          value={searchTerm}
          onChange={handleSearchChange}
          className="search-input"
        />
      </form>
    </div>
  );
};

export default Navbar;
