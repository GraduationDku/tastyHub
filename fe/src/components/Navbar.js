// components/Navbar.js
import React, { useState } from 'react';
import '../css/Navbar.css';

const Navbar = ({ setScreen, onSearchComplete }) => {
  const [searchTerm, setSearchTerm] = useState('');

  const handleSearchChange = (event) => {
    setSearchTerm(event.target.value);
  };

  const handleSearchSubmit = async (event) => {
    event.preventDefault();
    await fetchRecipes();
  };

  const fetchRecipes = async () => {
    const url = `http://localhost:8080/recipe/search/${searchTerm}`;
    try {
      const response = await fetch(url, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      if (response.ok) {
        const recipe = await response.json();
        onSearchComplete(recipe.content);  // Trigger search complete action
      } else {
        throw new Error('Failed to fetch recipes');
      }
    } catch (error) {
      console.error('Error fetching recipes:', error);
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
          placeholder="            레시피를 검색하세요 !"
          value={searchTerm}
          onChange={handleSearchChange}
          className="search-input"
        />
      </form>
    </div>
  );
};

export default Navbar;
