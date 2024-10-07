import React, { useState } from 'react';
import '../css/Navbar.css';
import { FaBars } from 'react-icons/fa';

const Navbar = ({ setScreen, onSearchComplete }) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [isUserSearch, setIsUserSearch] = useState(false);
  const [showMenu, setShowMenu] = useState(false); // 메뉴 표시 여부

  const handleSearchChange = (event) => {
    setSearchTerm(event.target.value);
  };

  const handleSearchSubmit = async (event) => {
    event.preventDefault();
    if (searchTerm.trim() === '') return; // 검색어가 비어있으면 종료

    if (isUserSearch) {
      const users = await fetchUsers(searchTerm);
      onSearchComplete(users);
    } else {
      const recipes = await fetchRecipes(searchTerm);
      onSearchComplete(recipes);
    }
    setSearchTerm(''); // 검색 후 입력란 비우기
    setShowMenu(false); // 검색 후 메뉴 숨기기
  };

  const fetchRecipes = async (searchTerm) => {
    const url = `${process.env.REACT_APP_API_URL}/recipe/search/${searchTerm}`;
    try {
      const response = await fetch(url);
      if (response.ok) {
        const data = await response.json();
        return data.content || [];
      } else {
        throw new Error('레시피를 가져오는 데 실패했습니다.');
      }
    } catch (error) {
      console.error('레시피 가져오기 오류:', error);
      return [];
    }
  };

  const fetchUsers = async (nickname) => {
    const url = `${process.env.REACT_APP_API_URL}/user/search-list?nickname=${nickname}`;
    try {
      const response = await fetch(url);
      if (response.ok) {
        const data = await response.json();
        return data.content || [];
      } else {
        throw new Error('사용자 정보를 가져오는 데 실패했습니다.');
      }
    } catch (error) {
      console.error('사용자 정보 가져오기 오류:', error);
      return [];
    }
  };

  const handleMenuItemClick = (screen) => {
    setScreen(screen);
    setShowMenu(false); // 메뉴 아이템 클릭 시 메뉴 숨기기
  };

  return (
    <div className="navbar">
      <div className="menu-button" onClick={() => setShowMenu(!showMenu)}>
        <FaBars size={24} color="#3EAB5C" />
      </div>

      {showMenu && (
        <div className="menu-content">
          <br/><br/>
          <form className="search-form" onSubmit={handleSearchSubmit}>
            <input
              type="text"
              placeholder={isUserSearch ? "사용자를 검색하세요!" : "레시피를 검색하세요!"}
              value={searchTerm}
              onChange={handleSearchChange}
              className="search-input"
              onKeyPress={(event) => {
                if (event.key === 'Enter') {
                  handleSearchSubmit(event); // 엔터 키 입력 시 검색 실행
                }
              }}
            />
            <button
              type="button"
              onClick={() => setIsUserSearch(!isUserSearch)} // 검색 모드 토글
              className="search-toggle-btn"
            >
              {isUserSearch ? "레시피 검색" : "사용자 검색"}
            </button>
          </form>

          <div className="navbar-buttons">
            <button className='main' onClick={() => handleMenuItemClick('main')}>TastyHub</button>
            <button onClick={() => handleMenuItemClick('recipe')}>레시피</button>
            <button onClick={() => handleMenuItemClick('post')}>재료 공유</button>
            <button onClick={() => handleMenuItemClick('mainchat')}>채팅</button>
            <button onClick={() => handleMenuItemClick('mypage')}>마이페이지</button>
          </div>
        </div>
      )}
    </div>
  );
};

export default Navbar;
