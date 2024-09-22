import React, { useState, useEffect } from 'react';
import '../css/MainScreen.css';

function MainScreen() {
  const [recipes, setRecipes] = useState([]);
  const [selectedRecipe, setSelectedRecipe] = useState(null);
  const [posts, setPosts] = useState([]);
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
        const url = `${process.env.REACT_APP_API_URL}/post/recent/list?page=${page}&size=${size}&sort=${sort}`;
        const response = await fetch(url, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          }
        });
        if (response.ok) {
          const data = await response.json();
          console.log('Received posts data:', data);
          if (Array.isArray(data.pagingPostResponseList)) {
            setPosts(data.pagingPostResponseList);
          } else {
            console.error('Data.pagingPostResponseList is not an array', data.pagingPostResponseList);
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
      <h1>주간 인기 레시피</h1>
      <div className='box'>
        <ul>
          {recipes.map((recipe) => (
            <li key={recipe.foodId} onClick={() => handleRecipeClick(recipe)}>
              {recipe.title}  {recipe.foodName}
            </li>
          ))}
        </ul>
      
        {selectedRecipe && (
          <div>
            <h2>{selectedRecipe.title}</h2>
            <img src={selectedRecipe.foodImgUrl} alt={selectedRecipe.foodName} style={{ width: '300px' }} />
            {selectedRecipe.foodInformationDto ? (
              <>
                <p className='info'>요리 시간: {selectedRecipe.foodInformationDto.cookingTime || 'N/A'}분 | {selectedRecipe.foodInformationDto.serving || 'N/A'}인분</p>
                <p></p>
                <p>{selectedRecipe.foodInformationDto.text || 'No description available'}</p>
              </>
            ) : (
              <p>No additional food information available.</p>
            )}
          </div>
        )}
      </div>
      <div className='posts'>
        <h1>실시간 게시글</h1>
        <ul>
          {posts.map((post) => (
            <li key={post.postId}>
              <h3>{post.title}</h3>
              <p>작성자: {post.userName}</p>
              <img src={post.userImg} alt={post.userName} style={{ width: '50px', height: '50px', borderRadius: '50%' }} />
              <p>상태: {post.postState}</p>
            </li>
          ))}
        </ul>
      </div>
    </body>
  );
}

export default MainScreen;