// components/Navbar.js
import React, { useState } from 'react';
import '../css/Navbar.css';

const Navbar = ({ setScreen, onSearchComplete }) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [isUserSearch, setIsUserSearch] = useState(false);

  const handleSearchChange = (event) => {
    setSearchTerm(event.target.value);
  };

  const handleSearchSubmit = async (event) => {
    event.preventDefault();
    if (isUserSearch) {
      await fetchUsers(searchTerm, 1, 10, 'asc');
    } else {
      await fetchRecipes();
    }
  };

  const fetchRecipes = async () => {
    const url = `${process.env.REACT_APP_API_URL}/recipe/search/${searchTerm}`;
    try {
      const response = await fetch(url, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      if (response.ok) {
        const recipe = await response.json();
        onSearchComplete(recipe.content);
      } else {
        throw new Error('레시피를 가져오는 데 실패했습니다.');
      }
    } catch (error) {
      console.error('레시피 가져오기 오류:', error);
    }
  };

  const fetchUsers = async (nickname, page, size, sort) => {
    const url = `${process.env.REACT_APP_API_URL}/user/search-list?nickname=${nickname}&page=${page}&size=${size}&sort=${sort}`;
    try {
      const response = await fetch(url, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      if (response.ok) {
        const data = await response.json();
        onSearchComplete(data.userDtoList);
        
        const authorization = response.headers.get('Authorization');
        const refreshToken = response.headers.get('Refresh');

        localStorage.setItem('accessToken', authorization);
      localStorage.setItem('refreshToken', refreshToken);
      } else {
        throw new Error('사용자 정보를 가져오는 데 실패했습니다.');
      }
    } catch (error) {
      console.error('사용자 정보 가져오기 오류:', error);
    }
  };

  return (
    <div className="navbar">
      <button className='main' onClick={() => setScreen('main')}>TastyHub</button>
      <button onClick={() => setScreen('recipe')}>레시피</button>
      <button onClick={() => setScreen('post')}>재료 공유</button>
      <button onClick={() => setScreen('mainchat')}>채팅</button>
      <button onClick={() => setScreen('mypage')}>마이페이지</button>

      <form className="search-form" onSubmit={handleSearchSubmit}>
        <input
          type="text"
          placeholder={isUserSearch ? "사용자를 검색하세요!" : "레시피를 검색하세요!"}
          value={searchTerm}
          onChange={handleSearchChange}
          className="search-input"
        />
        <button
          type="button"
          onClick={() => setIsUserSearch(!isUserSearch)} // 검색 모드 토글
          className="search-toggle-btn"
        >
          {isUserSearch ? "레시피 검색" : "사용자 검색"}
        </button>
        <button type="submit">검색</button>
      </form>
    </div>
  );
};

export default Navbar;
