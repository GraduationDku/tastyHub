import React, { useState } from 'react';
import '../css/Navbar.css';

const Navbar = ({ setScreen, onSearchComplete }) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [isUserSearch, setIsUserSearch] = useState(false);
  const [page, setPage] = useState(1); // 현재 페이지
  const [size, setSize] = useState(5); // 페이지 당 아이템 수 기본값
  const [sort, setSort] = useState('date'); // 정렬 방식 기본값
  const [totalItems, setTotalItems] = useState(0); // 전체 게시글 수
  const [totalPages, setTotalPages] = useState(0); // 전체 페이지 수
  const [posts, setPosts] = useState([]); // 검색 결과를 저장하는 상태
  const [nickname, setNickname] = useState('');

  const handleSearchChange = (event) => {
    setSearchTerm(event.target.value);
  };

  const handleSearchSubmit = async (event) => {
    event.preventDefault();
    if (isUserSearch) {
      const users = await fetchUsers(searchTerm);
      onSearchComplete(users);
    } else {
      const recipes = await fetchRecipes(searchTerm);
      onSearchComplete(recipes);
    }
  };

  const fetchRecipes = async (searchTerm, page = 1, sort = 'date', size = 5) => {
    const url = `${process.env.REACT_APP_API_URL}/recipe/search/${searchTerm}&page=${page}&size=${size}&sort=${sort}`;
    try {
      const response = await fetch(url);
      if (response.ok) {
        const data = await response.json();
        setPosts(data.content || []); // 기본값을 빈 배열로 설정
        setTotalItems(data.content ? data.content.length : 0);
        return data.content || []; // 기본값을 빈 배열로 설정
      } else {
        throw new Error('레시피를 가져오는 데 실패했습니다.');
      }
    } catch (error) {
      console.error('레시피 가져오기 오류:', error);
      return []; // 오류 발생 시 빈 배열 반환
    }
  };
  
  const fetchUsers = async (nickname, page = 1, sort = 'date', size = 5) => {
    const url = `${process.env.REACT_APP_API_URL}/user/search-list?nickname=${nickname}&page=${page}&size=${size}&sort=${sort}`;
    try {
      const response = await fetch(url);
      if (response.ok) {
        const data = await response.json();
        setPosts(data.content || []); // 기본값을 빈 배열로 설정
        setTotalItems(data.content ? data.content.length : 0);
        return data.content || []; // 기본값을 빈 배열로 설정
      } else {
        throw new Error('사용자 정보를 가져오는 데 실패했습니다.');
      }
    } catch (error) {
      console.error('사용자 정보 가져오기 오류:', error);
      return []; // 오류 발생 시 빈 배열 반환
    }
  };
  

  return (
    <div>
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
          {isUserSearch ? "사용자" : "레시피"}
        </button>
      </form>
      <div className='nav'>
        <button className='main' onClick={() => setScreen('main')}>TastyHub</button>
        <button onClick={() => setScreen('recipe')}>레시피</button>
        <button onClick={() => setScreen('post')}>재료 공유</button>
        <button onClick={() => setScreen('mainchat')}>채팅</button>
        <button onClick={() => setScreen('mypage')}>마이페이지</button>
      </div>
    </div>
  );
};

export default Navbar;
