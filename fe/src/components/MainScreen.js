import React, { useState, useEffect } from 'react';
import '../css/MainScreen.css';

function MainScreen({ onRecipeSelect, onPostSelect }) {
  const [recipes, setRecipes] = useState([]);
  const [selectedRecipe, setSelectedRecipe] = useState(null);
  const [posts, setPosts] = useState([]);
  const [selectPost, setSelectedPost] = useState(null);
  const [page, setPage] = useState();
  const [size, setSize] = useState();
  const [sort, setSort] = useState('');

  useEffect(() => {
    async function fetchWeeklyRecipes() {
      try {
        const response = await fetch(`${process.env.REACT_APP_API_URL}/recipe/popular?page=${page}&size=${size}&sort=${sort}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          }
        });
        if (response.ok) {
          const data = await response.json();
          console.log('Received recipes data:', data);
          if (Array.isArray(data.content)) {
            setRecipes(data.content);
          } else {
            console.error('Data.content is not an array', data.content);
          }
        } else {
          throw new Error('Failed to fetch recipes');
        }
      } catch (error) {
        console.error('Error fetching weekly recipes:', error);
      }
    }

    async function fetchRecentPosts() {
      try {
        console.log('Fetching recent posts...');
        const response = await fetch(`${process.env.REACT_APP_API_URL}/post/recent/list?page=${page}&size=${size}&sort=${sort}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization' : localStorage.getItem('accessToken')
          }
        });
        console.log('Response status:', response.status);
        if (response.ok) {
          const data = await response.json();
          console.log('Received posts data:', data.content);
          if (Array.isArray(data.content)) {
            setPosts(data.content);
          } else {
            console.error('Data is not an array', data);
          }
        } else {
          throw new Error('Failed to fetch posts');
        }
      } catch (error) {
        console.error('Error fetching recent posts:', error);
      }
    }

    fetchWeeklyRecipes();
    fetchRecentPosts();
  }, [page, size, sort]); // 페이지, 사이즈, 정렬 값이 변경될 때마다 호출

  const handleRecipeClick = (recipe) => {
    setSelectedRecipe(recipe);
    console.log('Selected recipe:', recipe);
  };

  return (
    <body>
      <br/><br/><br/>
      <h1 style={{fontSize:'23px'}}>오늘은 어떤 음식을 먹어볼까요 ? 🍴</h1>
      <div className='mainbox'>
  <div className='recipe-banner'>
    <ul>
      {recipes.map((recipe) => (
        <li key={recipe.foodId} onClick={() => onRecipeSelect(recipe.foodId)}>
          <img src={recipe.foodImgUrl} alt={selectedRecipe?.foodName || recipe.foodName} style={{ width: '100px' }} />
          <br/><br/>
          {recipe.title} {recipe.foodName}
        </li>
      ))}
    </ul>
  </div>
</div>

<h1 style={{fontSize:'23px'}}>지금 이런 재료를 공유하고 있어요 ! 🥦</h1>
<div className='posts'>
  <div className='post-banner'>
    <ul>
      {posts.map((post) => (
        <li key={post.postId} onClick={() => onPostSelect(post.postId)}>
          <h3>{post.title}</h3>
          <p>{post.userName}</p>
          <p>{post.postState}</p>
        </li>
      ))}
    </ul>
  </div>
</div>

    </body>
  );
}

export default MainScreen;
